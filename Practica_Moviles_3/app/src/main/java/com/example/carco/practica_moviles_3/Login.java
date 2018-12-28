package com.example.carco.practica_moviles_3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class Login extends AppCompatActivity
{

    EditText editTextUser;
    EditText editTextPassword;
    Button buttonLogin;
    TextView textViewInfo;

    Button buttonData;
    TextView textViewDatos;


    String user;
    String pass;
    String userPass;
    String horaConexion;
    Date currentTime;

    boolean usuarioValido = true;

    String IP = "http://sinaptycwebs.000webhostapp.com";
    String GET_BY_ID = IP + "/obtener_usuario_userpass.php";

    ObtenerWebService hiloconexion;

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
                intent.putExtras(b);

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

        buttonData = findViewById(R.id.buttonShowData);
        textViewDatos = findViewById(R.id.textViewDatos);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                user = editTextUser.getText().toString();
                pass = editTextPassword.getText().toString();
                userPass = user+pass;

                hiloconexion = new ObtenerWebService();
                String cadenallamada = GET_BY_ID + "?idalumno=" + userPass;
                hiloconexion.execute(cadenallamada,"2");   // Parámetros que recibe doInBackground


                textViewInfo.setText("Buscando Usuario, espere unos instantes...");
                buttonLogin.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(task, 3000);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        usuarioValido = false;
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve ="";

            if(params[1]=="2")
            {

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null)
                        {
                            result.append(line);
                        }

                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                        String resultJSON = respuestaJSON.getString("estado");

                        if (resultJSON=="1"){      // hay un alumno que mostrar
                            devuelve = devuelve + respuestaJSON.getJSONObject("alumno").getString("userPass");

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No hay alumnos";
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;

            }
            return "No se han podido obtener los datos";
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            textViewDatos.setText(s+ "nya");
            if(!s.equals("") )
            {
                usuarioValido = true;
            }
            else
            {
                usuarioValido = false;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
