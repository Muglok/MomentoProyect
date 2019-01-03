package com.dam.jesus.practica_2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//imports location tracking
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CrearMomento extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double latitude;
    double longitude;

    EditText titulo;
    EditText descripcion;
    EditText cancion;

    Button guardarMomento;

    String fechaActual;
    String horaActual;

    int idUsuario;
    int compartido;

    Momento2 momento;


    //estos son arrays para gestionar los permisos
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    LocationTrack constante;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_momento);

        //--- recuperamos variable id del login
        idUsuario = Login.id;

        //------------------- Location Tracking --------------------------
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        titulo = findViewById((R.id.editTextTitulo));
        descripcion = findViewById(R.id.editTextDescripcion);
        cancion = findViewById(R.id.editTextCancion);
        guardarMomento = findViewById(R.id.guardarMomento);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //Cogemos los permisos que no están todavía dados.
        //Los metemos en una lista para usarlos después.

        //VERSION_CODES.M es la version 23 de Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        //nos enlazamos con el boton de obtener geolocalización



        //creamos objeto tipo LocationTrack
        locationTrack = new LocationTrack(CrearMomento.this);

        if (locationTrack.canGetLocation())
        {
             longitude = locationTrack.getLongitude();
             latitude = locationTrack.getLatitude();

            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_LONG).show();
        }
        else
        {
            locationTrack.showSettingsAlert();
        }


        //Creamos un objeto LocationTrack llamado constante
        constante = new LocationTrack(CrearMomento.this);

        //???
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(LocationTrack.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(LocationTrack.EXTRA_LONGITUDE, 0);
                    }
                }, new IntentFilter(LocationTrack.ACTION_LOCATION_BROADCAST)
        );

        //------------------------------------------------------------------




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        guardarMomento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //-------------------------------- Insertar con Volley -------------------
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url ="http://momentosandroid.000webhostapp.com/momentosAndroid/insertar_momento.php";
                String tituloParam = titulo.getText().toString();
                String descripcionParam = descripcion.getText().toString();
                String cancionParam = cancion.getText().toString();

               fechaActual = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
               horaActual = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

               //idUsuario = 1;

               compartido = 0;

               //momento = new Momento2(idUsuario,tituloParam,descripcionParam,cancionParam,latitude,longitude,null,idUsuario,fechaActual,horaActual,compartido);


                // Mapeo de los pares clave-valor
                HashMap<String, Object> parametros = new HashMap<String,Object>();
                parametros.put("titulo", tituloParam);
                parametros.put("descripcion", descripcionParam);
                parametros.put("cancion", cancionParam);
                parametros.put("latitud",latitude);
                parametros.put("longitud",longitude);
                parametros.put("fecha",fechaActual);
                parametros.put("hora",horaActual);
                parametros.put("idusuario",idUsuario);
                parametros.put("compartido",compartido);



                JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        new JSONObject(parametros),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Manejo de la respuesta


                                //Accedemos al vector de resultados

                                String devuelve = "";

                                String resultJSON = null;   // estado es el nombre del campo en el JSON
                                try {
                                    resultJSON = response.getString("estado");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (resultJSON == "1") {      // hay un alumno que mostrar
                                    devuelve = "Momento insertado correctamente";
                                    Toast.makeText(getApplicationContext(), devuelve, Toast.LENGTH_LONG).show();

                                } else if (resultJSON == "2") {
                                    devuelve = "El alumno no pudo insertarse";
                                    //resultado.setText(devuelve);
                                }


                                //resultado.setText(devuelve);
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Manejo de errores
                                //resultado.setText("Ha habído un error");
                            }
                        });


                queue.add(jsArrayRequest);

                //------------------------------------------------------------------------


            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng miUbucacion = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(miUbucacion).title("Marker en mi ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbucacion));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miUbucacion, 12.0f));

    }


    //------------ de location tracking ------------------------------------------------
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("Estos permisos son obligatorios para la aplicación. Por favor acepta los permisos",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CrearMomento.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, LocationTrack.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, LocationTrack.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
        constante.stopListener();
    }
}
