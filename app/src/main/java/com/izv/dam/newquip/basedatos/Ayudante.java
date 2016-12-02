package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

public class Ayudante extends SQLiteOpenHelper {

    //sqlite
    //tipos de datos https://www.sqlite.org/datatype3.html
    //fechas https://www.sqlite.org/lang_datefunc.html
    //trigger https://www.sqlite.org/lang_createtrigger.html

    /*
     * Created by Nono on 10/10/2016.
     * TERMINADA la nueva base de datos
     */

    private static final int VERSION = 9;

    public Ayudante(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_NOTA, sql_LISTA, sql_MAPA;
        sql_NOTA = "create table if not exists " + ContratoBaseDatos.TablaNota.TABLA +
                " (" +
                ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement, " +
                ContratoBaseDatos.TablaNota.TITULO + " text, " +
                ContratoBaseDatos.TablaNota.NOTA + " text, " +
                ContratoBaseDatos.TablaNota.IMAGEN + " text, " +
                ContratoBaseDatos.TablaNota.VIDEO + " text, " +
                ContratoBaseDatos.TablaNota.AUDIO + " text, " +
                //ContratoBaseDatos.TablaNota.LISTA + " text, " +
                ContratoBaseDatos.TablaNota.FECHA_CREACION + " datetime, " +
                ContratoBaseDatos.TablaNota.FECHA_MODIFICACION + " datetime, " +
                ContratoBaseDatos.TablaNota.FECHA_RECORDATORIO + " datetime, " +
                ContratoBaseDatos.TablaNota.COLOR + " text " +
                ")";
        Log.v("sql_nota", sql_NOTA);
        db.execSQL(sql_NOTA);

        sql_LISTA = "create table if not exists " + ContratoBaseDatos.TablaLista.TABLA +
                " (" +
                ContratoBaseDatos.TablaLista._ID + " integer primary key autoincrement, " +
                ContratoBaseDatos.TablaLista.ID_NOTA + " integer, " +
                ContratoBaseDatos.TablaLista.TEXTO_LISTA + " text, " +
                ContratoBaseDatos.TablaLista.HECHO + " boolean " +
                ")";
        Log.v("sql_lista", sql_LISTA);
        db.execSQL(sql_LISTA);

        sql_MAPA = "create table if not exists " + ContratoBaseDatos.TablaMapa.TABLA +
                " (" +
                ContratoBaseDatos.TablaMapa._ID + " integer primary key autoincrement, " +
                ContratoBaseDatos.TablaMapa.ID_NOTA + " integer, " +
                ContratoBaseDatos.TablaMapa.HISTORIA + " text, " +
                ContratoBaseDatos.TablaMapa.LATITUD + " text, " +
                ContratoBaseDatos.TablaMapa.LONGITUD + " text " +
                ")";
        Log.v("sql_MAPA", sql_MAPA);
        db.execSQL(sql_MAPA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Crear tablas temporales
        //llevar los datos a las tablas temporales
        //borrar datos antiguas tablas
        //Crear tablas nuevas
        //llevar datos a las tablas nuevas

        String TablaNota = "drop table if exists " + ContratoBaseDatos.TablaNota.TABLA;
        String TablaLista = "drop table if exists " + ContratoBaseDatos.TablaLista.TABLA;
        String TablaMapa = "drop table if exists " + ContratoBaseDatos.TablaMapa.TABLA;
        Log.v("TablaNota", TablaNota);
        Log.v("TablaLista", TablaLista);
        db.execSQL(TablaNota);
        db.execSQL(TablaLista);
        db.execSQL(TablaMapa);
        onCreate(db);
    }
}