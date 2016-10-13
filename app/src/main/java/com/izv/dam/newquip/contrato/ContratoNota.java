package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Nota;

/**
 * Declaramos la interfaces que vamos a usar en el modelo-vista-presentador.
 */
public interface ContratoNota {

    interface InterfaceModelo {

        void close();

        Nota getNota(long id);

        long saveNota(Nota n);

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveNota(Nota n);

    }

    interface InterfaceVista {

        void mostrarNota(Nota n);

    }

}