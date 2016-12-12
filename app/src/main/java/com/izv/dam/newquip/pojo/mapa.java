package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/**
 * Created by dam on 2/12/16.
 */
public class mapa implements Parcelable {
    private long id_mapa;
    private long id_nota;
    private String historia, latitud, longitud;

    public mapa(long id_mapa, long id_nota, String historia, String latitud, String longitud) {
        this.id_mapa = id_mapa;
        this.id_nota = id_nota;
        this.historia = historia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public mapa() {
        this(0, 0, null, null, null);
    }

    public long getId_mapa() {
        return id_mapa;
    }

    public void setId_mapa(long id_mapa) {
        this.id_mapa = id_mapa;
    }

    public long getId_nota() {
        return id_nota;
    }

    public void setId_nota(long id_nota) {
        this.id_nota = id_nota;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public static Creator<mapa> getCREATOR() {
        return CREATOR;
    }

    protected mapa(Parcel in) {
        id_mapa = in.readLong();
        id_nota = in.readLong();
        historia = in.readString();
        latitud = in.readString();
        longitud = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id_mapa);
        dest.writeLong(id_nota);
        dest.writeString(historia);
        dest.writeString(latitud);
        dest.writeString(longitud);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<mapa> CREATOR = new Creator<mapa>() {
        @Override
        public mapa createFromParcel(Parcel in) {
            return new mapa(in);
        }

        @Override
        public mapa[] newArray(int size) {
            return new mapa[size];
        }
    };

    public ContentValues getContentValues() {
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId) {
        ContentValues valores = new ContentValues();
        if (withId)
            valores.put(ContratoBaseDatos.TablaMapa._ID, this.getId_mapa());
        valores.put(ContratoBaseDatos.TablaMapa.ID_NOTA, this.getId_nota());
        valores.put(ContratoBaseDatos.TablaMapa.HISTORIA, this.getHistoria());
        valores.put(ContratoBaseDatos.TablaMapa.LATITUD, this.getLatitud());
        valores.put(ContratoBaseDatos.TablaMapa.LONGITUD, this.getLongitud());
        return valores;
    } /* Metodo del cursor */

    public static mapa getMapa(Cursor c) {
        mapa objeto = new mapa();
        objeto.setId_mapa(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaMapa._ID)));
        objeto.setId_nota(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaMapa.ID_NOTA)));
        objeto.setHistoria(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaMapa.HISTORIA)));
        objeto.setLatitud(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaMapa.LATITUD)));
        objeto.setLongitud(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaMapa.LONGITUD)));
        return objeto;
    }
}