package com.dam.jesus.practica_2;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Registro extends AppCompatActivity {

    String user, pass, telefono;
    TextView txtNobmre, txtContraseña, txtTelefono;
    Button btnRegistro;
    boolean usuarioValido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().setTitle("Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNobmre = findViewById(R.id.editTextRegistroUser);
        txtContraseña = findViewById(R.id.editTextRegistroPassword);
        txtTelefono = findViewById(R.id.editTextRegistroTelefono);
        btnRegistro = findViewById(R.id.buttonRegistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                RegistarUsuario(view);

            }
        });
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
        }
    }

    public void RegistarUsuario(final View view)
    {
        String postRegistro = "http://momentosandroid.000webhostapp.com/momentosAndroid/insertar_usuario.php";

        user = txtNobmre.getText().toString();
        pass = txtContraseña.getText().toString();
        telefono = txtTelefono.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        HashMap<String, String> parametros = new HashMap();
        parametros.put("nombre", user);
        parametros.put("contrasenya", pass);
        parametros.put("userpass", user + pass);
        parametros.put("telefono", telefono);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                postRegistro,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        String devuelve = "";
                        try
                        {
                            String resultJSON = response.getString("estado");   // estado es el nombre del campo en el JSON


                            if (resultJSON=="1"){      // hay un alumno que mostrar

                                Snackbar.make(view, "Usuario registrado correctamente", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                            else if (resultJSON=="2"){
                                Snackbar.make(view, "No se ha podido registrar el usuario", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
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

        //------------------------------------------------------------------
    }
}
