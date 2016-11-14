package com.izv.dam.newquip.vistas.notas;

import android.content.Context;

import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

public class PresentadorNota implements ContratoNota.InterfacePresentador {

    private ContratoNota.InterfaceModelo modelo;
    private ContratoNota.InterfaceVista vista;

    public PresentadorNota(ContratoNota.InterfaceVista vista) {
        this.vista = vista;
        this.modelo = new ModeloNota((Context)vista);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public long onSaveNota(Nota n) {
       return this.modelo.saveNota(n);
    }

    @Override
    public void onDeleteNota(Nota n) {
        this.modelo.deleteNota(n);
    }

    @Override
    public void onSaveLista(Lista l) {
        this.modelo.saveLista(l);
    }

    @Override
    public void onDeleteLista(Lista l) {
        this.modelo.deleteLista(l);
    }

}