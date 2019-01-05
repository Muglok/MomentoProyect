package com.dam.jesus.practica_2;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import java.util.LinkedList;
import java.util.List;



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

    private Contacto contactoYo;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_READ_CONTACTS = 79;
    public static final int PERMISSION_REQUEST_CONTACT = 1;
    public static final int PERMISSION_REQUEST_CALL = 2;


    //----- Strings para conectar a web service -----------
    String postUserPass = "http://momentosandroid.000webhostapp.com/momentosAndroid/obtener_usuario_user_pass.php";


    //tarea que comprueba qué ha devuelto el web service y permite acceso a la app
    private Runnable task = new Runnable() {
        public void run() {

            buttonLogin.setEnabled(true);

            if (usuarioValido) {

                //------ Recuperamos teléfono del usuario y lo metemos en la base de datos -----

                askForContactPermissionLogin();

                if(contactoYo != null)
                {
                    updateTelefonoUsuario(id,contactoYo.getTelefono());
                }


                //------------------------------------------------------------------------------


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





    //-------------------------- parte contents provider ------------------------------------------
    public void getContactTelefono() {

        /*contactsCursor = getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI,   // URI de contenido para los contactos
        projection,                        // Columnas a seleccionar
        selectionClause                    // Condición del WHERE
        selectionArgs,                     // Valores de la condición
        sortOrder);                        // ORDER BY columna [ASC|DESC]*/

        String[] projeccion = new String[]{
                ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE};

        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";

        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                sortOrder);




        while (c.moveToNext()) {

            //----- cargar objeto contacto en ArratyList -------------
            if(c.getString(1).equals("Yo"))
            {
                contactoYo = new Contacto(c.getString(2),c.getString(1));
                //Toast.makeText(getApplicationContext(),contactoYo.getNombre()+" telefono: "+contactoYo.getTelefono(),Toast.LENGTH_LONG).show();
            }
            //---------------------------------------------------------
        }
        c.close();

    }


    public void askForContactPermissionLogin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("El acceso a los contactos es requerido");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Por favor confirme el acceso a los contactos");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                getContactTelefono();
            }
        } else {
            getContactTelefono();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactTelefono();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //ToastMaster.showMessage(this,"No permission for contacts");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSION_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //ObtenerDatosLlamadas();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //ToastMaster.showMessage(this,"No permission for contacts");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //------------------------------------------------------------------------------------


    public void updateTelefonoUsuario(int id, String telefono)
    {
        //---------- update con Volley ------------------
        String url = "http://momentosandroid.000webhostapp.com/momentosAndroid/actualizar_telefono.php";

        //queue
        RequestQueue queue = Volley.newRequestQueue(this);


        // Mapeo de los pares clave-valor
        HashMap<String,Object> parametros = new HashMap();
        parametros.put("telefono", telefono);
        parametros.put("id", id);

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
                            devuelve = "Telefono Actualizado";
                            Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();

                        } else if (resultJSON == "2") {
                            devuelve = "El momento no pudo modificarse";
                            //Toast.makeText(getApplicationContext(),devuelve,Toast.LENGTH_LONG).show();
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
        //-----------------------------------------------
    }
}
