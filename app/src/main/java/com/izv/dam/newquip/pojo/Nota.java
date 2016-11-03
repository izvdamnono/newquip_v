package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/*
 * Definimos las clases que van a afectar a la base de dato y las que usaremos de intermediario
 */
public class Nota implements Parcelable {

    private long id;
    private String titulo, nota, imagen,
            video, audio, fecha_creacion,
            fecha_modificacion, fecha_recordatorio, color;

    public Nota(long id, String titulo, String nota, String imagen, String video, String audio, String fecha_creacion, String fecha_modificacion, String fecha_recordatorio, String color) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
        this.imagen = imagen;
        this.video = video;
        this.audio = audio;
        this.fecha_creacion = fecha_creacion;
        this.fecha_modificacion = fecha_modificacion;
        this.fecha_recordatorio = fecha_recordatorio;
        this.color = color;
    }

    public Nota() {
        this(0, null, null, null, null, null, null, null, null, null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(String fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public String getFecha_recordatorio() {
        return fecha_recordatorio;
    }

    public void setFecha_recordatorio(String fecha_recordatorio) {
        this.fecha_recordatorio = fecha_recordatorio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nota='" + nota + '\'' +
                ", imagen='" + imagen + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", fecha_modificacion='" + fecha_modificacion + '\'' +
                ", fecha_recordatorio='" + fecha_recordatorio + '\'' +
                ", color='" + color + '\'' +
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
            valores.put(ContratoBaseDatos.TablaNota._ID, this.getId());
        }
        valores.put(ContratoBaseDatos.TablaNota.TITULO, this.getTitulo());
        valores.put(ContratoBaseDatos.TablaNota.NOTA, this.getNota());
        valores.put(ContratoBaseDatos.TablaNota.IMAGEN, this.getImagen());
        valores.put(ContratoBaseDatos.TablaNota.VIDEO, this.getVideo());
        valores.put(ContratoBaseDatos.TablaNota.AUDIO, this.getAudio());
        valores.put(ContratoBaseDatos.TablaNota.FECHA_CREACION, this.getFecha_creacion());
        valores.put(ContratoBaseDatos.TablaNota.FECHA_MODIFICACION, this.getFecha_modificacion());
        valores.put(ContratoBaseDatos.TablaNota.FECHA_RECORDATORIO, this.getFecha_recordatorio());
        valores.put(ContratoBaseDatos.TablaNota.COLOR, this.getColor());

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
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(nota);
        dest.writeString(imagen);
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(fecha_creacion);//datetime
        dest.writeString(fecha_modificacion);//datetime
        dest.writeString(fecha_recordatorio);//datetime
        dest.writeString(color);
    }

    /*
     * Metodo de la clase parcelabre que necesita lista
     */
    public Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        nota = in.readString();
        imagen = in.readString();
        video = in.readString();
        audio = in.readString();
        fecha_creacion = in.readString();//datetime
        fecha_modificacion = in.readString();//datetime
        fecha_recordatorio = in.readString();//datetime
        color = in.readString();
    }

    public static Creator<Nota> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };

    /*
     * Metodo del cursor
     */
    public static Nota getNota(Cursor c) {
        Nota objeto = new Nota();
        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNota._ID)));
        objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.NOTA)));
        objeto.setImagen(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.IMAGEN)));
        objeto.setVideo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.VIDEO)));
        objeto.setAudio(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.AUDIO)));
        objeto.setFecha_creacion(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA_CREACION)));
        objeto.setFecha_modificacion(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA_MODIFICACION)));
        objeto.setFecha_recordatorio(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA_RECORDATORIO)));
        objeto.setColor(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.COLOR)));
        return objeto;
    }

}