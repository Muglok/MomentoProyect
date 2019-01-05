package com.dam.jesus.practica_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Preferencias extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferencias);
    }

    @Override
    protected void onStop() {
        super.onStop();


        //reogemos las preferencias
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //las anadimos al string texto
        String userName = pref.getString("nombreUsuario", "user");
        String passWord = pref.getString("contrasena", "1234");

        Toast.makeText(this,userName+" -- "+passWord,Toast.LENGTH_LONG).show();
    }
}
