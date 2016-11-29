package com.izv.dam.newquip.vistas.main;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloQuip implements ContratoMain.InterfaceModelo {

    private GestionNota gn = null;
    private Cursor cursor;
    private Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;
    private Context c;

    public ModeloQuip(Context c) {
        //gn = new GestionNota(c);
        this.c = c;
    }

    @Override
    public void close() {
//        gn.close();
    }

    @Override
    public void loadData(OnDataLoadListener listener) {
        cursor = c.getContentResolver().query(uri, null, null, null, null);
        listener.setCursor(cursor);
    }

    @Override
    public Nota getNota(int position) {
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return n;
    }

    @Override
    public long deleteNota(Nota n) {
//        return gn.delete(n);
        String where = "_ID = " + n.getId();
        return c.getContentResolver().delete(uri, where, null);
    }

    @Override
    public long deleteNota(int position) {
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return this.deleteNota(n);
    }



}