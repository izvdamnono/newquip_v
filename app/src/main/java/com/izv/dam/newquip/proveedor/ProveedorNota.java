package com.izv.dam.newquip.proveedor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.util.UtilCadenas;

import static com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaNota.CONTENT_ITEM_TYPE;
import static com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaNota.CONTENT_TYPE;

public class ProveedorNota extends ContentProvider {

    private static final UriMatcher URI_MATCHER;
    private static final int TODO = 0;
    private static final int CONCRETO = 1;
    GestionNota gestionNota;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaNota.TABLA, 0);//Cursor
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaNota.TABLA + "/#", 1);//Un item
    }

    public ProveedorNota() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete = 0;
        switch (URI_MATCHER.match(uri)) {
            case CONCRETO:
                String id = uri.getLastPathSegment();
                String[] newSelectionArgs = UtilCadenas.getNewArray(selectionArgs, id);
                delete = gestionNota.delete(selection, newSelectionArgs);
                break;
            case TODO:
                delete = gestionNota.delete(selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error, la cagaste wey! :/");
        }
        if (delete > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delete;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int tipo = URI_MATCHER.match(uri);
        long id = 0;
        if (tipo == TODO) {
            id = gestionNota.insert(values);
        }
        if (id > 0) {
            Uri uriGeller = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
            return uriGeller;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int tipo = URI_MATCHER.match(uri);
        if (tipo == CONCRETO) {
            String id = uri.getLastPathSegment();
            selection = UtilCadenas.getCondicionesSql(selection, ContratoBaseDatos.TablaNota._ID + " = ?");
            selectionArgs = UtilCadenas.getNewArray(selectionArgs, id);
        }
        int valor = gestionNota.update(values, selection, selectionArgs);
        if (valor > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return valor;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        int tipo = URI_MATCHER.match(uri);
        if (tipo == CONCRETO) {
            String id = uri.getLastPathSegment();
            selection = UtilCadenas.getCondicionesSql(selection, ContratoBaseDatos.TablaNota._ID + " = ?");
            selectionArgs = UtilCadenas.getNewArray(selectionArgs, id);
        }
        c = gestionNota.getCursor(projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TODO:
                return CONTENT_TYPE;
            case CONCRETO:
                return CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        gestionNota = new GestionNota(getContext());
        return true;
    }
}
