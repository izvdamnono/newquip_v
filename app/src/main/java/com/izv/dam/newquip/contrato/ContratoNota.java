package com.izv.dam.newquip.contrato;

import android.database.Cursor;

import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;

import java.util.ArrayList;

/**
 * Declaramos la interfaces que vamos a usar en el modelo-vista-presentador.
 */
public interface ContratoNota {

    interface InterfaceModelo {

        void close();

        Nota getNota(int id);

        Lista getListas(int id);

        long saveNota(Nota n);

        long deleteNota(Nota n);

        long saveLista(Lista l);

        long deleteLista(Lista l);

        void loadData(OnDataLoadListener listener);

        interface OnDataLoadListener {
            public void setCursor(Cursor c);
        }
    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveNota(Nota n);

        void onDeleteNota(Nota n);

        void onSaveLista(Lista l);

        void onDeleteLista(Lista l);

    }

    interface InterfaceVista {

        void mostrarNota(Nota n);

        void mostrarListas(ArrayList<Lista> ls);

    }

}