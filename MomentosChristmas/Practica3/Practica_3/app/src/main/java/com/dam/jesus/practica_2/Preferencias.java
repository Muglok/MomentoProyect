package com.dam.jesus.practica_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
        String userName = pref.getString("nombreUsuario", Login.user);
        String passWord = pref.getString("contrasena", Login.pass);

        if(userName.length() == 0 || passWord.length() == 0)
        {
            if(userName.length() == 0)
            {
                userName.equals(Login.user);
            }

            if(passWord.length() == 0)
            {
                passWord.equals(Login.pass);
            }

        }
        else if(!userName.equals((Login.user)) || !passWord.equals(Login.pass))
        {
            actualizarUsuario(userName, passWord);
        }

    }

    public void actualizarUsuario(String userName, String passWord)
    {
        // ------------------ update user Pass Volley ----------------------------------
        String url = "http://momentosandroid.000webhostapp.com/momentosAndroid/actualizar_userPass.php";

        //queue
        RequestQueue queue = Volley.newRequestQueue(this);


        // Mapeo de los pares clave-valor
        HashMap<String,Object> parametros = new HashMap();
        parametros.put("nombre", userName);
        parametros.put("contrasenya", passWord);
        parametros.put("id", Login.id);

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
                            devuelve = "Datos de usuario actualizados";
                            Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();

                        } else if (resultJSON == "2") {
                            devuelve = "Error Fatal";
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


        //------------------------------------------------------------------------------
    }
}
