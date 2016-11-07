package com.izv.dam.newquip.vistas.notas;

import android.content.Context;

import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.gestion.GestionLista;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloNota implements ContratoNota.InterfaceModelo {

    private GestionNota gestionNota = null;
    private GestionLista gestionLista = null;

    public ModeloNota(Context c) {
        gestionNota = new GestionNota(c);
    }

    @Override
    public void close() {
        gestionNota.close();
    }

    @Override
    public Nota getNota(long id) {
        return gestionNota.get(id);
    }

    public Lista getLista(long id) {
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

    private long deleteNota(Nota n) {
        return gestionNota.delete(n);
    }

    private long insertNota(Nota n) {
        if (n.getNota().trim().compareTo("") == 0 && n.getTitulo().trim().compareTo("") == 0) {
            return 0;
        }
        return gestionNota.insert(n);
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