package com.izv.dam.newquip.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaMapa;
import com.izv.dam.newquip.pojo.Mapa;

/**
 * Created by dam on 2/12/16.
 */

public class GestionMapa extends Gestion<Mapa> {
    public GestionMapa(Context c) {
        super(c);
    }

    public GestionMapa(Context c, boolean write) {
        super(c, write);
    }

    @Override
    public long insert(Mapa objeto) {
        return this.insert(ContratoBaseDatos.TablaMapa.TABLA, objeto.getContentValues());
    }

    @Override
    public long insert(ContentValues objeto) {
        return this.insert(ContratoBaseDatos.TablaMapa.TABLA, objeto);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaMapa.TABLA);
    }

    @Override
    public int delete(Mapa objeto) {
        String condicion = ContratoBaseDatos.TablaMapa._ID + " = ?";
        String[] argumentos = {objeto.getId_mapa() + ""};
        return this.delete(ContratoBaseDatos.TablaMapa.TABLA, condicion, argumentos);
    }

    public int delete(String condicion, String[] argumentos) {
        return this.delete(ContratoBaseDatos.TablaMapa.TABLA, condicion, argumentos);
    }

    @Override
    public int update(Mapa objeto) {
        ContentValues valores = objeto.getContentValues();
        String condicion = ContratoBaseDatos.TablaMapa._ID + " = ?";
        String[] argumentos = {objeto.getId_mapa() + ""};
        return this.update(ContratoBaseDatos.TablaMapa.TABLA, valores, condicion, argumentos);
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return this.update(ContratoBaseDatos.TablaMapa.TABLA, valores, condicion, argumentos);
    }

    @Override
    public Cursor getCursor() {
        return this.getCursor(ContratoBaseDatos.TablaMapa.TABLA,
                ContratoBaseDatos.TablaMapa.PROJECTION_ALL,
                ContratoBaseDatos.TablaMapa.SORT_ORDER_DEFAULT
        );
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(ContratoBaseDatos.TablaMapa.TABLA, columns, selection, selectionArgs,
                groupBy, having, orderBy);
    }

    public Mapa get(long id) {
        String where = ContratoBaseDatos.TablaMapa._ID + " = ? ";
        String[] parametros = {id + ""};
        Cursor c = this.getCursor(ContratoBaseDatos.TablaMapa.PROJECTION_ALL, where, parametros,
                null, null, ContratoBaseDatos.TablaMapa.SORT_ORDER_DEFAULT);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return Mapa.getMapa(c);
        }
        return null;
    }
}
