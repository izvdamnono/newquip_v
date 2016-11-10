package com.izv.dam.newquip.vistas.notas;

import android.content.Context;
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
    private Context context;

    public ModeloNota(Context c) {
        gestionNota = new GestionNota(c);
        context = c;
    }

    @Override
    public void close() {
        gestionNota.close();
    }

    @Override
    public Nota getNota(long id) {
        return gestionNota.get(id);
    }

    @Override
    public Lista getListas(long id) {
        return gestionLista.get(id);
    }


    @Override
    public long saveNota(Nota n) {
        long r;
        if (n.getId() == 0) {
            r = this.insertNota(n);
        } else {
            r = this.updateNota(n);
        }
        return r;
    }

    @Override
    public long saveLista(Lista l) {
        Uri u = context.getContentResolver().insert(ContratoBaseDatos.CONTENT_URI_LISTA, l.getContentValues());
        String s = u.getLastPathSegment();
        long id = Long.parseLong(s);
        return id;
    }

    private long deleteNota(Nota n) {
        return gestionNota.delete(n);
    }

    private long insertNota(Nota n) {
        if (n.getNota().trim().compareTo("") == 0 && n.getTitulo().trim().compareTo("") == 0) {
            return 0;
        }
        return gestionNota.insert(n);
    }

    private long insertLista(Lista l) {
        if (l.getId_lista() == 0 && l.getId_nota() == 0) {
            return 0;
        }
        return gestionLista.insert(l);
    }

    private long updateNota(Nota n) {
        if (n.getNota().trim().compareTo("") == 0 && n.getTitulo().trim().compareTo("") == 0) {
            this.deleteNota(n);
            gestionNota.delete(n);
            return 0;
        }
        return gestionNota.update(n);
    }
}