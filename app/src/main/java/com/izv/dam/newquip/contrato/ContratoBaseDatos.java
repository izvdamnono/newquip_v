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
        //public static final String TEXTO = "texto";
        public static final String IMAGEN = "imagen";
        public static final String VIDEO = "video";
        public static final String AUDIO = "audio";
        //public static final String LISTA = "LISTA";
        public static final String FECHA_CREACION = "fecha_creacion";
        public static final String FECHA_MODIFICACION = "fecha_modificacion";
        public static final String COLOR = "color";
        public static final String[] PROJECTION_ALL = {_ID, TITULO, NOTA, IMAGEN, VIDEO, AUDIO, FECHA_CREACION, FECHA_MODIFICACION};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";
    }
}