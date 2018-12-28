package com.example.carco.practica_moviles_3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper
{
    String sqlCreate = "CREATE TABLE Accesos (codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            " usuario TEXT, horaConexion TEXT)";

    public BD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
