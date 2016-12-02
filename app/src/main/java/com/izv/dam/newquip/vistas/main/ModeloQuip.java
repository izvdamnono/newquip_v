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

    /*
        @Override
        public void loadData(OnDataLoadListener listener) {
            cursor = c.getContentResolver().query(uri, null, null, null, null);
            listener.setCursor(cursor);
        }
    */
    @Override
    public void loadData(OnDataLoadListener listener, String filtro) {
        String[] s = {filtro};
        cursor = c.getContentResolver().query(uri, null, filtro, null, null);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                System.out.println(Nota.getNota(cursor).toString());
            }
        } else {
            System.out.println("cursor is null");
        }

        listener.setCursor(cursor);

        //query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
        //dao
        // select proyeccion from tabla where seleccion order by ordenacion
        //seleccion: campo = ? and otrocampo = ?
        //selectionArgs: {st1, st2}
        //select campo
        //from tabla
        //where condicion/es
        //order by campos
        //clase DataBase -> db->select()->from()->where()->order()
    }

    @Override
    public Nota getNota(int position) {
        cursor.moveToPosition(position);
        return Nota.getNota(cursor);
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