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

public class ContentsProvider extends AppCompatActivity implements View.OnClickListener {


    Button contactos, llamadas;
    TextView resultado;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_READ_CONTACTS = 79;
    public static final int PERMISSION_REQUEST_CONTACT = 1;
    public static final int PERMISSION_REQUEST_CALL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_provider);

        contactos = findViewById(R.id.contactos);
        llamadas = findViewById(R.id.llamadas);
        resultado = findViewById(R.id.resultado);

        contactos.setOnClickListener(this);
        llamadas.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.contactos:
                askForContactPermission();
                break;
            case R.id.llamadas:
                askForCallPermission();
                break;
        }
    }



    public void ObtenerDatosLlamadas() {

        Uri uri;

        /*
        content://media/internal/images
        content://media/external/video
        content://media/internal/audio

        */

        //         content://media/*/images
        //         content://settings/system/ringtones

        uri = Uri.parse("content://call_log/calls");

        String[] projeccion = new String[]{
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION};


        Cursor c = getContentResolver().query(
                uri,
                projeccion,
                null,
                null,
                null);

        resultado.setText("");


        while (c.moveToNext()) {
            resultado.append("Tipo: " +
                    c.getString(0) +
                    " Número: " +
                    c.getString(1) +
                    " Duración: " +
                    c.getString(2) + "\n");
        }
        c.close();


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

        resultado.setText("");


        while (c.moveToNext()) {
            resultado.append("Identificador: " +
                    c.getString(0) +
                    " Nombre: " +
                    c.getString(1) +
                    " Número: " +
                    c.getString(2) +
                    " Tipo: " +
                    c.getString(3) + "\n");

            //----- cargar objeto contacto en ArratyList -------------

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
                ObtenerDatosLlamadas();
            }
        } else {
            ObtenerDatosLlamadas();
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
                    ObtenerDatosLlamadas();
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
}
