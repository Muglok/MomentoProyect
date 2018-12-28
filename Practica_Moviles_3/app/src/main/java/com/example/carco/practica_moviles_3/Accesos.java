package com.example.carco.practica_moviles_3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Accesos extends AppCompatActivity {

    TextView textViewRegistros;
    String datosAccesos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesos);

        Bundle b2 = this.getIntent().getExtras();
        datosAccesos = b2.getString("datosSelect");
        textViewRegistros = findViewById(R.id.textViewRegistros);
        textViewRegistros.setText(datosAccesos);

    }

}
