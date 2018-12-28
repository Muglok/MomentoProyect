package com.example.carco.practica_moviles_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    TextView textViewContentMain;
    MediaPlayer mediaPlayer;
    boolean musicOn;
    ImageView imageViewContentMain;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String horaConexion;
    String user;
    String pass;

    SQLiteDatabase db;
    String datosSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = this.getIntent().getExtras();
        horaConexion = b.getString("horaConexion");
        user = b.getString("user");
        pass = b.getString("pass");

        BD accesos =
                new BD(this, "BDAccesos", null, 1);

        db = accesos.getWritableDatabase();

        if(db != null)
        {
            db.execSQL("INSERT INTO Accesos (usuario,horaConexion) " +
                    "VALUES ('" + user + "','"+horaConexion+"')");
        }

        String[] args = new String[]
                {"VACIO"};

        Cursor c = db.rawQuery(" SELECT * FROM Accesos WHERE usuario != ? ", args);


        if (c.moveToFirst())
        {
            datosSelect="";

            do
            {
                Integer id= c.getInt(0);
                String user = c.getString(1);
                String hora = c.getString(2);

                datosSelect += " User: " + user + " Hora: " + hora + "\n";

            } while(c.moveToNext());
        }

        textViewContentMain = (TextView) findViewById(R.id.textViewContentMain);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mediaPlayer =  mediaPlayer.create(this,R.raw.cancion1);

        imageViewContentMain = (ImageView) findViewById(R.id.imageViewContentMain);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String mensajeDatosAlumno = "Marcos Cervantes \n carcosg8@gmail.com \n 2ºDAM Presencial \n IES San Vicente";

        int id = item.getItemId();

        if (id == R.id.datos_alumno) {
            textViewContentMain.setText(mensajeDatosAlumno);
            textViewContentMain.setVisibility(View.VISIBLE);
            imageViewContentMain.setVisibility(View.GONE);
            return true;

        }

        if (id == R.id.preferencias) {
            String texto = "";
            texto = "Preferencias selecconada";
            textViewContentMain.setText(texto);
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

            texto += "Uso de sonido: " + pref.getBoolean("usoSonido", false) + "\n";
            texto += "Nombre de usuario: " + pref.getString("nombreUsuario", "user") + "\n";
            texto += "Nombre de usuario: " + pref.getString("contrasena", "1234") + "\n";
            texto += "Opcion 3: " + pref.getString("opcion3", "default") + "\n";
            texto += "Cancion elegída: " + pref.getString("listaCanciones", "default") + "\n";

            textViewContentMain.setText(texto);
            return true;
        }

        if(id == R.id.play){

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String cancion = pref.getString("listaCanciones", "default");

            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if(pref.getBoolean("usoSonido",false) == false)
            {

                manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
            else
            {
                manager.setStreamMute(AudioManager.STREAM_MUSIC, false);

                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                }

                switch (cancion){

                    case "c1":
                        mediaPlayer =  mediaPlayer.create(this,R.raw.cancion1);
                        break;

                    case "c2":
                        mediaPlayer =  mediaPlayer.create(this,R.raw.cancion2);
                        break;

                    case "c3":
                        mediaPlayer =  mediaPlayer.create(this,R.raw.cancion3);
                        break;

                    case "c4":
                        mediaPlayer =  mediaPlayer.create(this,R.raw.cancion4);
                        break;

                    default:
                        mediaPlayer =  mediaPlayer.create(this,R.raw.musica);
                        break;
                }
                mediaPlayer.start();
                musicOn = true;
            }

            return true;
        }

        if(id == R.id.pause){
            mediaPlayer.stop();
            musicOn = false;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

            textViewContentMain.setVisibility(View.GONE);
            imageViewContentMain.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_registro) {

            Intent intent = new Intent(MainActivity.this,Accesos.class);

            Bundle b2 = new Bundle();
            b2.putString("datosSelect",datosSelect);
            intent.putExtras(b2);

            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //onPause
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"Salta el onPause",Toast.LENGTH_SHORT).show();
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Salta el onResume",Toast.LENGTH_SHORT).show();

        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(musicOn)
        {
            if(pref.getBoolean("usoSonido",false) == true) {
                mediaPlayer.start();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewContentMain.setImageBitmap(imageBitmap);
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        Toast.makeText(this, "Salta el Stop", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
