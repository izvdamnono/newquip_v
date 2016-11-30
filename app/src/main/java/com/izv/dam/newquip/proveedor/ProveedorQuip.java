package com.izv.dam.newquip.proveedor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.gestion.GestionLista;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.util.UtilCadenas;

import java.util.Arrays;

import static com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaNota.CONTENT_ITEM_TYPE_NOTA;
import static com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaNota.CONTENT_TYPE_NOTA;

import static com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaLista.CONTENT_ITEM_TYPE_LISTA;
import static com.izv.dam.newquip.contrato.ContratoBaseDatos.TablaLista.CONTENT_TYPE_LISTA;

public class ProveedorQuip extends ContentProvider {
    private static final UriMatcher URI_MATCHER;
    private static final int TODO_NOTA = 0;
    private static final int CONCRETO_NOTA = 1;
    private static final int TODO_LISTA = 2;
    private static final int CONCRETO_LISTA = 3;
    GestionNota gestionNota;
    GestionLista gestionLista;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaNota.TABLA, TODO_NOTA);//Cursor
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaNota.TABLA + "/#", CONCRETO_NOTA);//Un item
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaLista.TABLA, TODO_LISTA);//Cursor
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaLista.TABLA + "/#", CONCRETO_LISTA);//Un item
    }

    public ProveedorQuip() {
//        gestionNota = new GestionNota(getContext());
//        gestionLista = new GestionLista(getContext());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id;
        String[] newSelectionArgs;
        int delete = 0;
        switch (URI_MATCHER.match(uri)) {
            case CONCRETO_NOTA:
                id = uri.getLastPathSegment();
                newSelectionArgs = UtilCadenas.getNewArray(selectionArgs, id);
                delete = gestionNota.delete(selection, newSelectionArgs);
//                delete = gestionNota.delete(ContratoBaseDatos.TablaNota._ID + " = ?", new String[]{id});
                break;

            case TODO_NOTA:
                delete = gestionNota.delete(selection, selectionArgs);
                break;

            case CONCRETO_LISTA:
                id = uri.getLastPathSegment();
                newSelectionArgs = UtilCadenas.getNewArray(selectionArgs, id);
                delete = gestionLista.delete(selection, newSelectionArgs);
//                delete = gestionLista.delete(ContratoBaseDatos.TablaLista._ID + " = ?", new String[]{id});
                break;

            case TODO_LISTA:
                delete = gestionLista.delete(selection, selectionArgs);
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
        long id = 0;
        switch (URI_MATCHER.match(uri)) {
            case TODO_NOTA:
                id = gestionNota.insert(values);
                break;
            case TODO_LISTA:
                id = gestionLista.insert(values);
                break;

            default:
                throw new IllegalArgumentException("Error, la cagaste wey! :/ (En el insert)");
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
        String id;
        String[] newSelectionArgs;
        int update = 0;
        switch (URI_MATCHER.match(uri)) {
            case CONCRETO_NOTA:
                id = uri.getLastPathSegment();
                selection = UtilCadenas.getCondicionesSql(selection, ContratoBaseDatos.TablaNota._ID + " = ? ");
                newSelectionArgs = UtilCadenas.getNewArray(selectionArgs, id);

//                System.out.println("selection " + selection);
//                System.out.println("values " + values.toString());
//                System.out.println("selectionArgs " + Arrays.toString(newSelectionArgs));

                update = gestionNota.update(values, selection, newSelectionArgs);
                break;

            case TODO_NOTA:
                update = gestionNota.update(values, selection, selectionArgs);
                break;
            case CONCRETO_LISTA:
                id = uri.getLastPathSegment();
                selection = UtilCadenas.getCondicionesSql(selection, ContratoBaseDatos.TablaLista._ID + " = ? ");
                newSelectionArgs = UtilCadenas.getNewArray(selectionArgs, id);

                update = gestionLista.update(values, selection, newSelectionArgs);
                break;
            case TODO_LISTA:
                update = gestionLista.update(values, selection, selectionArgs);
                break;
        }
//        System.out.println("update " + update);
        if (update > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return update;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        String id;
        switch (URI_MATCHER.match(uri)) {
            case CONCRETO_NOTA:
                id = uri.getLastPathSegment();
                selection = UtilCadenas.getCondicionesSql(selection, ContratoBaseDatos.TablaNota._ID + " = ?");
                selectionArgs = UtilCadenas.getNewArray(selectionArgs, id);

                c = gestionNota.getCursor(projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CONCRETO_LISTA:
                id = uri.getLastPathSegment();
                selection = UtilCadenas.getCondicionesSql(selection, ContratoBaseDatos.TablaNota._ID + " = ?");
                selectionArgs = UtilCadenas.getNewArray(selectionArgs, id);

                c = gestionLista.getCursor(projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        c = gestionNota.getCursor(projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TODO_NOTA:
                return CONTENT_TYPE_NOTA;
            case CONCRETO_NOTA:
                return CONTENT_ITEM_TYPE_NOTA;
            case TODO_LISTA:
                return CONTENT_TYPE_LISTA;
            case CONCRETO_LISTA:
                return CONTENT_ITEM_TYPE_LISTA;

            default:
                throw new IllegalArgumentException("Error, la cagaste wey! :/ (En el getType)");
        }

    }

    @Override
    public boolean onCreate() {
        gestionNota = new GestionNota(getContext());
        gestionLista = new GestionLista(getContext());
        return true;
    }
}
