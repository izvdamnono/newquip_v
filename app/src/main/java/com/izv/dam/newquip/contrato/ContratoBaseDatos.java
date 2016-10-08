package com.izv.dam.newquip.contrato;

import android.provider.BaseColumns;

/**
 * Generamos la clase abstracta de la base de datos
 */
public class ContratoBaseDatos {

    public final static String BASEDATOS = "quiip.sqlite";

    private ContratoBaseDatos() {
    }

    public static abstract class TablaNota implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "nota";
        public static final String TITULO = "titulo";
        public static final String NOTA = "nota";
        public static final String[] PROJECTION_ALL = {_ID, TITULO, NOTA};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";
    }
}