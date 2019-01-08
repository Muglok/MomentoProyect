package com.dam.jesus.practica_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class Accesos extends AppCompatActivity {

    TextView textViewRegistros;
    String datosAccesos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesos);
        getSupportActionBar().setTitle("Historial de Accesos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recupero datos del bundle pasado de la ventana Login
        Bundle b2 = this.getIntent().getExtras();
        datosAccesos = b2.getString("datosSelect");

        textViewRegistros = findViewById(R.id.textViewRegistros);
        textViewRegistros.setMovementMethod(new ScrollingMovementMethod());

        textViewRegistros.setText(datosAccesos);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }    }
}
