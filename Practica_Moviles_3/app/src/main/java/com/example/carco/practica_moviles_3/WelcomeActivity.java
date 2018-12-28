package com.example.carco.practica_moviles_3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private Runnable task = new Runnable()
    {
        public void run() {
            Toast.makeText(getApplicationContext(), "Pasamos al login!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WelcomeActivity.this,Login.class);

            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler handler = new Handler();
        handler.postDelayed(task, 3000);
    }
}
