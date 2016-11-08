package com.izv.dam.newquip.pojo;

import android.content.ContentValues;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/**
 * Created by Nono on 10/10/2016.
 * TERMINADA
 */

public class Lista implements Parcelable {
    private long id_lista, id_nota;
    private String texto_lista;
    private boolean hecho;

    public Lista(long id_lista, long id_nota, String texto_lista, boolean hecho) {
        this.id_lista = id_lista;
        this.id_nota = id_nota;
        this.texto_lista = texto_lista;
        this.hecho = hecho;
    }

    public Lista() {
        this(0, 0, null, false);
    }

    public long getId_lista() {
        return id_lista;
    }

    public void setId_lista(long id_lista) {
        this.id_lista = id_lista;
    }


    public long getId_nota() {
        return id_nota;
    }

    public void setId_nota(long id_nota) {
        this.id_nota = id_nota;
    }

    public String getTexto_lista() {
        return texto_lista;
    }

    public void setTexto_lista(String texto_lista) {
        this.texto_lista = texto_lista;
    }

    public boolean isHecho() {
        return hecho;
    }

    public void setHecho(boolean hecho) {
        this.hecho = hecho;
    }

    @Override
    public String toString() {
        return "Lista{" +
                "id_lista=" + this.getId_lista() +
                ", id_nota=" + this.getId_nota() +
                ", texto_lista='" + this.getTexto_lista() + '\'' +
                ", hecho=" + this.isHecho() +
                '}';
    }

    /*
     * Metodos de ContentValues
     */
    public ContentValues getContentValues() {
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId) {
        ContentValues valores = new ContentValues();
        if (withId) {
            valores.put(ContratoBaseDatos.TablaLista._ID, this.getId_lista());
        }
        valores.put(ContratoBaseDatos.TablaLista.ID_NOTA, this.getId_nota());
        valores.put(ContratoBaseDatos.TablaLista.TEXTO_LISTA, this.getTexto_lista());
        valores.put(ContratoBaseDatos.TablaLista.HECHO, this.isHecho());

        return valores;
    }

    /*
     * Metodos que la clase parcelable usa
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id_lista);
        dest.writeLong(id_nota);
        dest.writeString(texto_lista);
        /*
         * No existe el metodo  writeBoolean() pero si el writeByte, con el operador ternario
         * transformo el boolean en byte
         */
        dest.writeByte((byte) (hecho ? 1 : 0));
    }

    /*
     * Metodo de la clase parcelabre que necesita lista
     */
    protected Lista(Parcel in) {
        id_lista = in.readLong();
        id_nota = in.readLong();
        texto_lista = in.readString();
        hecho = in.readByte() != 0;
    }

    public static Creator<Lista> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Lista> CREATOR = new Creator<Lista>() {
        @Override
        public Lista createFromParcel(Parcel in) {
            return new Lista(in);
        }

        @Override
        public Lista[] newArray(int size) {
            return new Lista[size];
        }
    };

    public static Lista getLista(Cursor c) {
        Lista objeto = new Lista();
        objeto.setId_lista(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista._ID)));
        objeto.setId_nota(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista.ID_NOTA)));
        objeto.setTexto_lista(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaLista.TEXTO_LISTA)));

        String hecho = c.getString(c.getColumnIndex(ContratoBaseDatos.TablaLista.HECHO));
        objeto.setHecho(Boolean.valueOf(hecho));

        return objeto;
    }


}
