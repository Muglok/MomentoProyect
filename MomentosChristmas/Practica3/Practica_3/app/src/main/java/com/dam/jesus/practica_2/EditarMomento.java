package com.dam.jesus.practica_2;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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

public class EditarMomento extends AppCompatActivity implements OnMapReadyCallback {

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

    EditText etTiulo, etDescripcion, etCancion;
    TextView tvFecha, tvHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_momento);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //---- Me enlazo con los elementos que van a recibir datos ----------------
        etTiulo         = findViewById(R.id.editTextModificarTitulo);
        etDescripcion   = findViewById(R.id.editTextModificarDescripcion);
        etCancion       = findViewById(R.id.editTextModificarCancion);
        tvFecha         = findViewById(R.id.textViewModificarFecha);
        tvHora          = findViewById(R.id.textViewModificarHora);
        etDescripcion.setMovementMethod(new ScrollingMovementMethod());
        //-------------------------------------------------------------------------

        //------ Recibo datos de la activity Ver Momento --------------------------
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
        //-------------------------------------------------------------------------


        //----- damos valor a los campos--------------------------------------------
        etTiulo.setText(titulo);
        etDescripcion.setText(descripcion);
        etCancion.setText(cancion);
        tvFecha.setText(fechaActual);
        tvHora.setText(horaActual);
        //------------------------------------------------------------------------

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEditarM);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarMomento();
                Snackbar.make(view, "Momento modificado correctamente", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Lugar del Momento"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void modificarMomento()
    {
        //---------- update con Volley ------------------
        String url = "http://momentosandroid.000webhostapp.com/momentosAndroid/modificar_momento.php";

        //queue
        RequestQueue queue = Volley.newRequestQueue(this);


        // Mapeo de los pares clave-valor
        HashMap<String,Object> parametros = new HashMap();
        parametros.put("titulo", etTiulo.getText().toString());
        parametros.put("descripcion", etDescripcion.getText().toString());
        parametros.put("cancion", etCancion.getText().toString());
        parametros.put("id", idMomento);

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
                            devuelve = "Momento Modificado";
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
