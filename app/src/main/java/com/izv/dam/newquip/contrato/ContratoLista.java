package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Lista;

/**
 * Created by Nono on 10/10/2016.
 * FALTA LOS METODOS DE LISTA
 */

public interface ContratoLista {

    interface InterfaceModelo {

        void close();

        Lista getLista(long id);

        long saveLista(Lista l);
    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveLista(Lista n);

    }

    interface InterfaceVista {

        void mostrarLista(Lista n);

    }
}
