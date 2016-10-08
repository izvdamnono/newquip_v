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
    /**
     %d		day of month: 00
     %f		fractional seconds: SS.SSS
     %H		hour: 00-24
     %j		day of year: 001-366
     %J		Julian day number
     %m		month: 01-12
     %M		minute: 00-59
     %s		seconds since 1970-01-01
     %S		seconds: 00-59
     %w		day of week 0-6 with Sunday==0
     %W		week of year: 00-53
     %Y		year: 0000-9999
     %%		%
     */
    //trigger https://www.sqlite.org/lang_createtrigger.html

    private static final int VERSION = 1;

    public Ayudante(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table if not exists " + ContratoBaseDatos.TablaNota.TABLA +
                " (" +
                ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaNota.TITULO + " text, " +
                ContratoBaseDatos.TablaNota.NOTA + " text " +
                ")";
        Log.v("sql",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists " + ContratoBaseDatos.TablaNota.TABLA;
        db.execSQL(sql);
        Log.v("sql",sql);
    }
}