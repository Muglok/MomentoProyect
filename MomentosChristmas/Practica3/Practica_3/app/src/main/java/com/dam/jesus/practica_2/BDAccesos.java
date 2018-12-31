package com.dam.jesus.practica_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDAccesos extends SQLiteOpenHelper {


    String sqlCreate = "CREATE TABLE Accesos (" +
            "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " usuario TEXT," +
            " horaConexion TEXT)";


    public BDAccesos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //ejecute el comando que le hemos pasado (crear tabla)
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
