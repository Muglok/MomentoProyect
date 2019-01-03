package com.dam.jesus.practica_2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VerMomento extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



   //--------- Variables a recibir --------------------
    String titulo;
    String descripcion;
    String cancion;
    double latitude;
    double longitude;
    String fechaActual;
    String horaActual;
    int idMomento;
    int compartido;

    int idUsuario;
    //-------------------------------------------------

    TextView tvTiulo, tvDescripcion, tvCancion, tvFecha, tvHora;

    Button compartirM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_momento);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTiulo = findViewById(R.id.editTextTitulo);
        tvDescripcion = findViewById(R.id.editTextDescripcion);
        tvCancion = findViewById(R.id.editTextCancion);
        tvFecha = findViewById(R.id.editTextFecha);
        tvHora = findViewById(R.id.editTextHora);

        //compartirM = findViewById((R.id.compartirMomento));


        //---------------- Recupero los datos pasados desde la Main Activity -------------------
        /*
            Pendiente de parte MainActivity

             Bundle b = this.getIntent().getExtras();
            titulo = b.getString("titulo");
            descripcion = b.getString("descripcion");
            cancion = b.getString("cancion");
            latitude = b.getDouble("latitude");
            longitude = b.getDouble("longitude");
            fechaActual = b.getString("fechaActual");
            horaActual = b.getString("horaActual");
            idMomento = b.getInt("idMomento");
            compartido = b.getInt("compartido");

        //idUsuario la cogemos de la activity login
        idUsuario = Login.id;

         */

        //--------------------------------------------------------------------------------------
        /*
           Plantilla de cómo vienen los datos del MainActivity

           b.putString("titulo",momento.getTitulo());
           b.putString("descripcion",momento.getDescripcion());
           b.putString("cancion",momento.getCancion());
           b.putDouble("latitude",momento.getLatitud());
           b.putDouble("longitude",momento.getLongitud());
           b.putString("fechaActual",momento.getFecha());
           b.putString("horaActual",momento.getHora());
           b.putInt("idMomento",momento.getId());
           b.putInt("compartido",momento.getCompartido());
         */

        //Colocamos datos en textViews
        /*
        tvTiulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        tvCancion.setText(cancion);
        tvFecha.setText(fechaActual);
        tvHora.setText(horaActual);
        */


        //------------------------------ Metemos datos de prueba -------------------------
        tvTiulo.setText("Momento de Prueba");
        tvDescripcion.setText("Descripción momento de prueba");
        tvCancion.setText("Help - The Beatles");
        tvFecha.setText("03-01.2019");
        tvHora.setText("04:25:47");
        // latitud y longitud de prueba
        latitude =  -10.56789;
        longitude = 20.5678;

        compartido = 0;
        idMomento = 3;
        //-------------------------------------------------------------------------------
    }


    public void compartirMomento(View v) {
        // Do your stuff
        if(compartido == 0)
        {
            compartirEsteMomento(idMomento);
        }
        else
        {
            Toast.makeText(this,"Este momento ya está compartido",Toast.LENGTH_SHORT).show();
        }

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
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Lugar del momento"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void compartirEsteMomento(int idDelMomento)
    {
        //--------- volley Update -----------------------

        String url = "http://momentosandroid.000webhostapp.com/momentosAndroid/compartir_momento.php";

        //queue
        RequestQueue queue = Volley.newRequestQueue(this);


        // Mapeo de los pares clave-valor
       HashMap<String,Integer> parametros = new HashMap();
        parametros.put("compartido", 1);
        parametros.put("id", idDelMomento);

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
                            devuelve = "Momento Compartido";
                            Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();

                        } else if (resultJSON == "2") {
                            devuelve = "El momento no pudo modificarse";
                            Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        Toast.makeText(getApplicationContext(),"Ha habído un error",Toast.LENGTH_LONG).show();
                    }
                });


        queue.add(jsArrayRequest);
        //-----------------------------------------------
    }

}
