package com.izv.dam.newquip.vistas.notas;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.gestion.GestionLista;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloNota implements ContratoNota.InterfaceModelo {

    private GestionNota gestionNota = null;
    private GestionLista gestionLista = null;
    private Cursor cursor_nota;
    private Cursor cursor_lista;
    private Uri uri_nota = ContratoBaseDatos.CONTENT_URI_NOTA;
    private Uri uri_lista = ContratoBaseDatos.CONTENT_URI_LISTA;
    private Context c;

    public ModeloNota(Context c) {
//        gestionNota = new GestionNota(c);
//        gestionLista = new GestionLista(c);
        this.c = c;
    }

    @Override
    public void close() {
//        gestionNota.close();
    }

    @Override
    public void loadData(OnDataLoadListener listener) {
        cursor_nota = c.getContentResolver().query(uri_nota, null, null, null, null);
        listener.setCursor(cursor_nota);
        cursor_lista = c.getContentResolver().query(uri_lista, null, null, null, null);
        listener.setCursor(cursor_lista);
    }

    @Override
    public Nota getNota(int id) {
        cursor_nota.moveToPosition(id);
        return Nota.getNota(cursor_nota);
    }

    @Override
    public Lista getListas(int id) {
        cursor_lista.moveToPosition(id);
        return Lista.getLista(cursor_lista);
    }


    @Override
    public long saveNota(Nota n) {
        if (n.getId() == 0) {
            return this.insertNota(n);
        }
        return this.updateNota(n);
    }

    @Override
    public long deleteNota(Nota n) {
//        return gestionNota.delete(n);
        String where = "_ID = " + n.getId();
        return c.getContentResolver().delete(uri_nota, where, null);
    }

    @Override
    public long saveLista(Lista l) {
        if (l.getId_lista() == 0) {
            return this.insertLista(l);
        }
        return this.updateLista(l);
    }

    @Override
    public long deleteLista(Lista l) {
//        return gestionLista.delete(l);
        String where = "_ID = " + l.getId_lista();
        return c.getContentResolver().delete(uri_lista, where, null);
    }


    private long insertNota(Nota n) {
        if (n.getNota().trim().compareTo("") == 0 && n.getTitulo().trim().compareTo("") == 0) {
            return 0;
        }
//        return gestionNota.insert(n);
        String where = "_ID = " + n.getId();
        Uri u = c.getContentResolver().insert(uri_nota, n.getContentValues());
        if (u.toString() != null) {
            return 1;
        } else {
            return 0;
        }

    }

    private long insertLista(Lista l) {
        if (l.getId_lista() == 0 && l.getId_nota() == 0) {
            return 0;
        }
//        return gestionLista.insert(l);
        Uri u = c.getContentResolver().insert(uri_lista, l.getContentValues());
        if (u.toString() != null) {
            return 1;
        } else {
            return 0;
        }
    }

    private long updateNota(Nota n) {
        if (n.getNota().trim().compareTo("") == 0 && n.getTitulo().trim().compareTo("") == 0) {
//            this.deleteNota(n);
//            gestionNota.delete(n);
            this.deleteNota(n);
            return 0;
        }

//        return gestionNota.update(n);
        String where = "_ID = " + n.getId();
        return c.getContentResolver().update(uri_nota, n.getContentValues(), null, null);
    }

    private long updateLista(Lista l) {
        if (l.getTexto_lista() != null && l.getTexto_lista().trim().compareTo("") == 0) {
//            this.deleteLista(l);
//            gestionLista.delete(l);
            this.deleteLista(l);
            return 0;
        }
//        return gestionLista.update(l);
        String where = "_ID = " + l.getId_lista();
        return c.getContentResolver().update(uri_lista, l.getContentValues(), null, null);
    }
}