
package com.dam.jesus.practica_2;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.dam.jesus.practica_2.MainActivity;
import com.dam.jesus.practica_2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Login extends AppCompatActivity {


    public static int id;

    EditText editTextUser;
    EditText editTextPassword;
    Button buttonLogin;
    TextView textViewInfo;

    String user;
    String pass;

    String telefono;

    String horaConexion;
    Date currentTime;

    Usuario currentUser;

    boolean usuarioValido = false;




    //----- Strings para conectar a web service -----------
    String postUserPass = "http://momentosandroid.000webhostapp.com/momentosAndroid/obtener_usuario_user_pass.php";



    //tarea que comprueba qué ha devuelto el web service y permite acceso a la app
    private Runnable task = new Runnable()
    {
        public void run() {

            buttonLogin.setEnabled(true);

            if(usuarioValido)
            {

                currentTime = Calendar.getInstance().getTime();
                horaConexion = currentTime.toString();


                Intent intent = new Intent(Login.this,MainActivity.class);
                Bundle b = new Bundle();
                b.putString("horaConexion",horaConexion);
                b.putString("user",user);
                b.putString("pass",pass);
                b.putString("telefono",telefono);
                b.putInt("id",id);
                intent.putExtras(b);
                // abro ventana
                startActivity(intent);

            }
            else
            {
                textViewInfo.setText("Usuario no válido, intentelo de nuevo");
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUser = findViewById(R.id.editTextUser);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewInfo = findViewById(R.id.textViewInfo);

        //boton login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                user = editTextUser.getText().toString();
                pass = editTextPassword.getText().toString();

                //----------------- Volley POST User Pass -------------------------
                //queue
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                //recogemos params

                //Mapeo de los pares clave-valor
                HashMap<String, String>   parametros = new HashMap();
                parametros.put("nombre", user);
                parametros.put("contrasenya", pass);

                JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        postUserPass,
                        new JSONObject(parametros),
                        new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {

                                // Manejo de la respuesta
                                //Accedemos al vector de resultados
                                String devuelve = "";

                                try
                                {
                                    //Accedemos al vector de resultados

                                    String resultJSON = response.getString("estado");   // estado es el nombre del campo en el JSON

                                    //resultado.setText(resultJSON);

                                    if (resultJSON=="1"){      // hay un alumno que mostrar

                                        usuarioValido = true;

                                        //Creamos un objeto de ese usuario
                                         id = response.getJSONObject("usuario").getInt("id");
                                         telefono = response.getJSONObject("usuario").getString("telefono");

                                        //-- Creamos objeto usuario
                                        //currentUser = new Usuario(id,user,pass,telefono);
                                    }
                                    else if (resultJSON=="2"){
                                        usuarioValido = false;
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

                textViewInfo.setText("Buscando Usuario, espere unos instantes...");
                buttonLogin.setEnabled(false);

                //ejecutamos tarea en un handler
                Handler handler = new Handler();
                handler.postDelayed(task, 2000);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        usuarioValido = false;
    }
}
