package com.dam.jesus.practica_2;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Momento> list;
    public static ArrayList<Momento2> list_momentos;
    public static RecyclerView recyclerMomentos;

    MediaPlayer mediaPlayer;
    boolean musicOn;
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

        list = new ArrayList<>();
        list_momentos = new ArrayList<>();
        construirRecycler2();


        //Recupero datos del bundle pasado de la ventana Login
        Bundle b = this.getIntent().getExtras();
        horaConexion = b.getString("horaConexion");
        user = b.getString("user");
        pass = b.getString("pass");

        //Toast.makeText(this,"Hora conexion recibida: "+horaConexion+" usuario: "+user,Toast.LENGTH_LONG).show();

        //------------------------- BBDD --------------------------------
        //Enlazo con la base de Accesos
        BDAccesos accesos =
                new BDAccesos(this, "BDAccesos", null, 1);

        db = accesos.getWritableDatabase();

        //insertamos datos de acceso
        if(db != null)
        {
            db.execSQL("INSERT INTO Accesos (usuario,horaConexion) " +
                    "VALUES ('" + user + "','"+horaConexion+"')");
        }

        //Hacemos consulta
        String[] args = new String[]
                {"VACIO"};
        //Creamos un cursor con el editText
        Cursor c = db.rawQuery(" SELECT * FROM Accesos WHERE usuario != ? ", args);

        //volcamos  los resultados de cada linea en datosSelect
        if (c.moveToFirst())
        {
            datosSelect=""; // Vacio el textview
            //Recorremos el cursor hasta que no haya más registros
            do
            {
                Integer id= c.getInt(0);
                String user = c.getString(1);
                String hora = c.getString(2);

                //sacamos por pantalla cada registro
                datosSelect += " User: " + user + " Hora: " + hora + "\n";

            } while(c.moveToNext());
        }

        //-----------------------------------------------------------------------
        //Cerrar BD
        //db.close();

        //Con esto enlazamos los controles de volumen con el stream music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);


        //enalazo con la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //cargo mediaPlayer
        mediaPlayer =  mediaPlayer.create(this,R.raw.cancion1);;

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



    private void construirRecycler()
    {
        recyclerMomentos = findViewById(R.id.RecyclerId);
        recyclerMomentos.setLayoutManager(new LinearLayoutManager(this));
        llenarMomentos2();
    }

    private void construirRecycler2()
    {
        recyclerMomentos = findViewById(R.id.RecyclerId);
        recyclerMomentos.setLayoutManager(new LinearLayoutManager(this));
        llenarMomentos3();
        AdapterMomentos2 adapter = new AdapterMomentos2(list_momentos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Seleccion: " + list_momentos.get
                                (recyclerMomentos.getChildAdapterPosition(view))
                                .getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerMomentos.setAdapter(adapter);
    }

    private void llenarMomentos2()
    {
        String query = "http://momentosandroid.000webhostapp.com/momentosAndroid/obtener_usuarios.php";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject respuestaJSON = new JSONObject(response);
                    String resultJSON = respuestaJSON.getString("estado");

                    int[] fotos = new int[5];
                    fotos[0] = R.drawable.aut_1;
                    fotos[1] = R.drawable.delete_dis;
                    fotos[2] = R.drawable.dino_1;
                    fotos[3] = R.drawable.pingu;
                    fotos[4] = R.drawable.omnovos_dafuq_is_that;
                    int num;

                    if (resultJSON=="1")
                    {
                        JSONArray alumnosJSON = respuestaJSON.getJSONArray("usuarios");
                        for(int i=0;i<alumnosJSON.length();i++)
                        {
                            num = i % 3;
                            list.add(new Momento(alumnosJSON.getJSONObject(i).getString("nombre"),alumnosJSON.getJSONObject(i).getString("telefono"),fotos[num]));

                        }
                    }
                    AdapterMomento adapter = new AdapterMomento(list);

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),
                                    "Seleccion: " + list.get
                                            (recyclerMomentos.getChildAdapterPosition(view))
                                            .getNombre(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    recyclerMomentos.setAdapter(adapter);
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
            }
        });

        queue.add(stringRequest);


    }

    private void llenarMomentos()
    {
        list.add(new Momento("Gato","A diferencia del de “Oblígame prro”, estos dinosaurios no salieron de una caricatura, en realidad son parte de un juego de Facebook llamado Jurassic Park Builder, un videojuego basado en la franquicia cinematográfica de 1993 y que te permite construir tus propio parque temático con animales extintos. ¿La buena noticia? Jurassic Park Builder está disponible tanto para Android como para iOS.",R.drawable.aut_1));
        list.add(new Momento("Delete dis","¿La Rosa de Guadalupe se inspiró en este meme para crear un capítulo?\n" +
                "\n" +
                "Recorre en internet un video de un capítulo de La Rosa de Guadalupe cuyo capítulo se titula \"Cállese Viejo Lesbiano\". Esto es falso. No existe. Si entran a Wikipedia a ver la lista de capítulos, ninguno lleva ese nombre.",R.drawable.delete_dis));
        list.add(new Momento("Dino","A diferencia del de “Oblígame prro”, estos dinosaurios no salieron de una caricatura, en realidad son parte de un juego de Facebook llamado Jurassic Park Builder, un videojuego basado en la franquicia cinematográfica de 1993 y que te permite construir tus propio parque temático con animales extintos. ¿La buena noticia? Jurassic Park Builder está disponible tanto para Android como para iOS.\n" +
                "\n" +
                "En las redes sociales existen diversos orígenes para estos dinomemes, sobretodo para el famosos de ellos, el \"Cállese Viejo Lesbiano\".",R.drawable.dino_1));
        list.add(new Momento("Dafuq","Si eres de los que tiene la fortuna de no estar pegado al internet las 24 horas quizás aún no te hayas topado con los dino-memes, pero no te preocupes porque aquí te decimos de qué van.\n" +
                "\n" +
                "Se tratan de imágenes de dinosaurios en 3D con curiosas frases o estúpidas. Depende cómo los quieras ver. Muchas de ellas nos provocan mucha risa.\n" +
                "\n" +
                "Si de plano no logran ubicarlos mejor les dejamos el más famoso que dice la finísima frase “Cállese viejo lesbiano”",R.drawable.nota_2));
        list.add(new Momento("Dafuq","La plataforma de videos más popular liberó el trailer de Caballeros del Zodiaco y las redes sociales ardieron con los comentarios de todos los fans. Y es que nadie esperaba ver lo que terminaron viendo.\n" +
                "\n" +
                "Ya sea por el estilo de animación o el trabajo de algunas nuevas voces, incluyendo la de Seiya, existe mucho espacio para que los fans de la versión original disparen sus dardos. Más aún, de seguro uno de los elementos que más comentarios generará será el hecho de adaptar a Shun, el caballero de Andrómeda, como mujer.\n" +
                "\n" +
                "Vean el tráiler a continuación, en el que por alguna razón hay tanques y artillería pesada atacando a los santos de bronce.",R.drawable.omnovos_dafuq_is_that));
        list.add(new Momento("Pingu","Internet es un mar profundo de memes. Desde sus origenes hemos visto pasar diversos memes y los Dinomemes son las  últimas tendencias en convertirse viral en las redes sociales. ¿De dónde salieron los Dinomemes? ¿Cómo se originaron? ¿Quién los creó? ¡¡Cállese Viejo Lesbiano!!",R.drawable.pingu));
        list.add(new Momento("whatafuq","¿\"Cállese Viejo Lesbiano nació de un reality en donde una joven acusa a un señor de acosarla?\n" +
                "\n" +
                "También es falso. Existe la historia que ´la frase viene de una entrevista en donde una chica es acosada por un señor, y para poder huir de él, ella le dice que es lesbiana, a lo que él le contesta que también es \"lesbiano\" y continua acosándola. Entonces alguien le grita al señor \"¡cállese, viejo lesbiano!\"",R.drawable.photo5834437103243603928));
    }

    private void llenarMomentos3()
    {
        list_momentos.add(new Momento2(1,"Fiesta de año nuevo","Fiesta en casa de gudetama celebrando el 2019","Dino crisis soundtrack",383451700,-0.4814900,new Date("31/12/2018"),2));
        list_momentos.add(new Momento2(2,"Music Rock festival","Concierto rock en el campus de la UA","Fiesta pagana",383451700,-0.4814900,new Date("05/08/2018 12:25"),4));
        list_momentos.add(new Momento2(3,"Estreno mundial Avengers 17","Otra pelicula mas","NO",383451700,-0.4814900,new Date("22/05/2018 13:05"),5));
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //------------------------------------------------------------------
    //----- Menu Superior Actionbar (Musica, Preferncias, Datos Alumno)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String mensajeDatosAlumno = "Jesús Salas Hellín \n jsalasdam@gmail.com \n 2ºDAM Presencial \n IES San Vicente";

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.datos_alumno) {
            return true;
        }

        if (id == R.id.preferencias) {
            String texto = "";
            texto = "Le hemos dado a preferencias";

            //Aqui hacemos un intent para abrir la activity de preferencias
            Intent intent = new Intent(this, Preferencias.class);
            startActivity(intent);

            //reogemos las preferencias
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            //las anadimos al string texto
            texto += "Uso de sonido: " + pref.getBoolean("usoSonido", false) + "\n";
            texto += "Nombre de usuario: " + pref.getString("nombreUsuario", "user") + "\n";
            texto += "Nombre de usuario: " + pref.getString("contrasena", "1234") + "\n";
            texto += "Opcion 3: " + pref.getString("opcion3", "default") + "\n";
            texto += "Cancion elegída: " + pref.getString("listaCanciones", "default") + "\n";
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


    //-----------------------------------------------------
    //------ menu lateral Navigation Drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Hago una foto
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }


        } else if (id == R.id.nav_accesos) {
            //Vamos a Activity Accesos
            Intent intent = new Intent(MainActivity.this,Accesos.class);
            //creo bundle
            Bundle b2 = new Bundle();
            b2.putString("datosSelect",datosSelect);
            intent.putExtras(b2);
            // abro ventana
            startActivity(intent);

        } else if (id == R.id.nav_ContentsProvider) {
            //Vamos a Activity Accesos
            Intent intent = new Intent(MainActivity.this,ContentsProvider.class);
            // abro ventana
            startActivity(intent);

        }

        else if (id == R.id.nav_crear_momento) {
            //Vamos a Activity Accesos
            Intent intent = new Intent(MainActivity.this,CrearMomento.class);
            // abro ventana
            startActivity(intent);

        }

        else if (id == R.id.nav_manage) {

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
        //Guardo datos
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }

    }

    //onResume
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Salta el onResume",Toast.LENGTH_SHORT).show();
        //Recupero datos
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
            //imageViewContentMain.setImageBitmap(imageBitmap);
        }
    }

    //onStop
    @Override
    protected void onStop()
    {
        super.onStop();
        Toast.makeText(this, "Salta el Stop", Toast.LENGTH_SHORT).show();
        //Recupero datos
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
    }






}
