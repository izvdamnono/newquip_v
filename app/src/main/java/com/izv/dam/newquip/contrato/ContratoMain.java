package com.izv.dam.newquip.contrato;

import android.database.Cursor;

import com.izv.dam.newquip.pojo.Nota;

/**
 * ¿Definimos los metodos del modelo vista presentador?
 */
public interface ContratoMain {

    interface InterfaceModelo {

        void close();

        Nota getNota(int position);

        long deleteNota(int position);

        long deleteNota(Nota n);

//        void loadData(OnDataLoadListener listener);

        void loadData(OnDataLoadListener listener, String filtro);

        interface OnDataLoadListener {
            public void setCursor(Cursor c);
        }
    }

    interface InterfacePresentador {

        void onAddNota();

        void onPause();

        void onResume();

        void onResume(String filtro);

        void onDeleteNota(int position);

        void onDeleteNota(Nota n);

        void onEditNota(int position);

        void onEditNota(Nota n);

        void onShowBorrarNota(int position);

    }

    interface InterfaceVista {

        void mostrarAgregarNota();

        void mostrarDatosFiltrados(Cursor c);

        void mostrarDatos(Cursor c);

        void mostrarEditarNota(Nota n);

        void mostrarConfirmarBorrarNota(Nota n);

    }

}