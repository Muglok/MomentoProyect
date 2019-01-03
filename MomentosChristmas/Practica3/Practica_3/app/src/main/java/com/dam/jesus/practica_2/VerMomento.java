package com.dam.jesus.practica_2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VerMomento extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



   //--------- Variables a recibir ------------------__
    String titulo;
    String descripcion;
    String cancion;
    double latitude;
    double longitude;
    String fechaActual;
    String horaActual;
    int idUsuario;
    int idMomento;
    int compartido;
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

        tvTiulo.setText("Momento de Prueba");
        tvDescripcion.setText("Descripción momento de prueba");
        tvCancion.setText("Help - The Beatles");
        tvFecha.setText("03-01.2019");
        tvHora.setText("04:25:47");
    }


    public void compartirMomento(View v) {
        // Do your stuff
        Toast.makeText(this,"Falta compartir momento",Toast.LENGTH_SHORT).show();
        //
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Lugar del momento"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
