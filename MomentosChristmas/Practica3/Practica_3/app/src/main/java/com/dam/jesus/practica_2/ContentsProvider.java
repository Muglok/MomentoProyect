package com.dam.jesus.practica_2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ContentsProvider extends AppCompatActivity {


    Button contactos, llamadas;
    TextView resultado;

    String listaNumeros = "";


    ArrayList<String> numerosUsuarios;
    Set<Contacto> contactosAgenda;
    ArrayList<Contacto> contactosConApp;

    Contacto contactoAgenda;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_READ_CONTACTS = 79;
    public static final int PERMISSION_REQUEST_CONTACT = 1;
    public static final int PERMISSION_REQUEST_CALL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_provider);


        resultado = findViewById(R.id.resultado);


        numerosUsuarios = new ArrayList<>();
        contactosAgenda = new HashSet<>();
        contactosConApp = new ArrayList<>();


        resultado.setText("iniciando: ");
        obtenerNumerosUsuarios();


        //recorrer 2 listas


        /*
        for(String numero : numerosUsuarios)
        {
            listaNumeros += numero + " \n ";
        }

        resultado.setText(listaNumeros);
        */

        //resultado.setText("Hola Amigos");

        //askForContactPermission();

    }



    public void getContact() {

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

        //resultado.setText("");


        while (c.moveToNext()) {
            /*
            resultado.append("Identificador: " +
                    c.getString(0) +
                    " Nombre: " +
                    c.getString(1) +
                    " Número: " +
                    c.getString(2) +
                    " Tipo: " +
                    c.getString(3) + "\n");
            */

            //----- cargar objetos contacto en ArratyList de contactos de la agenda -------------

                String numeroSinEspacion = c.getString(2).replace(" ","");

                 contactoAgenda = new Contacto(numeroSinEspacion,c.getString(1));
                 contactosAgenda.add(contactoAgenda);

            //---------------------------------------------------------
        }
        c.close();

    }


    public void askForContactPermission() {
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
                getContact();
            }
        } else {
            getContact();
        }
    }

    public void askForCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CALL_LOG)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("El acceso a las llamadas es requerido");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Por favor confirme el acceso a las llamadas");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CALL_LOG}
                                    , PERMISSION_REQUEST_CALL);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CALL_LOG},
                            PERMISSION_REQUEST_CALL);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

            }
        } else {

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
                    getContact();
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


    public void obtenerNumerosUsuarios()
    {

        //----------------- get All volley ---------------------------

        String url ="http://momentosandroid.000webhostapp.com/momentosAndroid/obtener_usuarios.php";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                String telefono = "";
                String devuelve ="";

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                try
                {
                    //resultado.setText(response);

                    JSONObject respuestaJSON = new JSONObject(response);

                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                    //resultado.setText(resultJSON);

                    if (resultJSON=="1")
                    {      // hay alumnos a mostrar
                        JSONArray alumnosJSON = respuestaJSON.getJSONArray("usuarios");   // estado es el nombre del campo en el JSON
                        for(int i=0;i<alumnosJSON.length();i++)
                        {
                            telefono = alumnosJSON.getJSONObject(i).getString("telefono");
                            numerosUsuarios.add(telefono);
                        }
                        /*
                        // telefonos del servicio web
                        String genteConMiApp = "Estos son los numeros rescatados de la base de datos";
                        for(String telef : numerosUsuarios)
                        {
                            genteConMiApp += telef + " \n ";
                        }

                        resultado.setText(genteConMiApp);
                        */

                        //---------------- cargar lista teléfonos -------------------------------
                        askForContactPermission();
                        /*
                        // comprobamos contactos agenda
                        String genteConMiApp = "Estos son los contactos de la agenda: \n";
                        for(Contacto ct : contactosAgenda)
                        {
                            genteConMiApp += ct.getNombre() + " - "+ct.getTelefono()+ " \n ";
                        }

                        resultado.setText(genteConMiApp);
                        */


                        //---------------- Comparar listas y crear lista contactos con mi app -----

                        for(Contacto ct: contactosAgenda)
                        {
                            for(String tfn: numerosUsuarios)
                            {
                                if(ct.getTelefono().equals(tfn))
                                {
                                    contactosConApp.add(ct);
                                }
                            }
                        }


                        //------------- pasamos datos a hashSet -------------------
                        Set<String> setContactos = new HashSet<>();

                        for(Contacto cont : contactosConApp)
                        {
                            setContactos.add(cont.getNombre()+";"+cont.getTelefono());
                        }

                        //------ mostrar hashSet en resultado -----------------------------
                        String genteConMiApp = "Contactos con Geo Moments: \n";

                        Iterator<String> i = setContactos.iterator();
                        while (i.hasNext())
                            genteConMiApp += i.next() +"\n";

                        resultado.setText(genteConMiApp);


                    }
                    else if (resultJSON=="2"){
                        devuelve = "No hay alumnos";
                        //resultado.setText(devuelve);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String s = "No se pudo realizar la solicitud";
                resultado.setText(s);
            }
        });

        queue.add(stringRequest);

        //---------------------------------------------------------------------

    }






}
