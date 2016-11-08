package com.izv.dam.newquip.contrato;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Generamos la clase abstracta de la base de datos
 */
public class ContratoBaseDatos {
    ///storage/emulated/0/databaseIZV/
    public final static String BASEDATOS = "quiip.sqlite";
    //ProveedorNota
    public static final String AUTORIDAD = "com.izv.dam.newquip.proveedor";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTORIDAD);
    public static final Uri CONTENT_URI_NOTA = Uri.withAppendedPath(CONTENT_URI, TablaNota.TABLA);
    public static final Uri CONTENT_URI_LISTA = Uri.withAppendedPath(CONTENT_URI, TablaLista.TABLA);

    private ContratoBaseDatos() {
    }

    public static abstract class TablaNota implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "nota";
        public static final String TITULO = "titulo";
        public static final String NOTA = "nota";
        public static final String IMAGEN = "imagen";
        public static final String VIDEO = "video";
        public static final String AUDIO = "audio";
        public static final String FECHA_CREACION = "fecha_creacion";
        public static final String FECHA_MODIFICACION = "fecha_modificacion";
        public static final String FECHA_RECORDATORIO = "fecha_recordatorio";
        public static final String COLOR = "color";
        public static final String[] PROJECTION_ALL = {_ID, TITULO, NOTA, IMAGEN, VIDEO, AUDIO,
                FECHA_CREACION, FECHA_MODIFICACION, FECHA_RECORDATORIO, COLOR};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

        //Tipos MIME
        public final static String CONTENT_ITEM_TYPE_NOTA = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd" + AUTORIDAD + "." + TABLA;
        public final static String CONTENT_TYPE_NOTA = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" + AUTORIDAD + "." + TABLA;

    }

    public static abstract class TablaLista implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "lista";
        public static final String ID_NOTA = "id_nota";
        public static final String TEXTO_LISTA = "TEXTO_LISTA";
        public static final String HECHO = "hecho";
        public static final String[] PROJECTION_ALL = {_ID, ID_NOTA, TEXTO_LISTA, HECHO};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

        //Tipos MIME
        public final static String CONTENT_ITEM_TYPE_LISTA = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd" + AUTORIDAD + "." + TABLA;
        public final static String CONTENT_TYPE_LISTA = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" + AUTORIDAD + "." + TABLA;
    }
}