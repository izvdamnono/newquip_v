package com.izv.dam.newquip.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.izv.dam.newquip.pojo.Lista;


public class GestionLista extends Gestion<Lista>{
    public GestionLista(Context c) {
        super(c);
    }

    public GestionLista(Context c, boolean write) {
        super(c, write);
    }

    @Override
    public long insert(Lista objeto) {
        return 0;
    }

    @Override
    public long insert(ContentValues objeto) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int delete(Lista objeto) {
        return 0;
    }

    @Override
    public int update(Lista objeto) {
        return 0;
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return 0;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return null;
    }
}
