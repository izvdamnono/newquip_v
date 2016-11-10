package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

import java.util.ArrayList;

/**
 * Declaramos la interfaces que vamos a usar en el modelo-vista-presentador.
 */
public interface ContratoNota {

    interface InterfaceModelo {

        void close();

        Nota getNota(long id);

        Lista getListas(long id);

        long saveNota(Nota n);

        long saveLista(Lista l);

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveNota(Nota n);

        long onSaveLista(Lista l);

    }

    interface InterfaceVista {

        void mostrarNota(Nota n);

        void mostrarListas(ArrayList<Lista> ls);

    }

}