package com.dam.jesus.practica_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Bienvenida extends AppCompatActivity {


    //tarea que lanza una nueva activity
    private Runnable task = new Runnable()
    {
        public void run() {
            Intent intent = new Intent(Bienvenida.this,Login.class);
            // abro ventana
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        //ejecutamos tarea en un handler
        Handler handler = new Handler();
        handler.postDelayed(task, 1500);
    }
}



