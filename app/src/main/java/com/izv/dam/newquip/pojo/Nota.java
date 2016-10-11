package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/*
    Definimos las clases que van a afectar a la base de dato y las que usaremos de intermediario
 */
public class Nota implements Parcelable {

    private long id;
    private String titulo, nota, image, video, audio, fecha_creacion, fecha_modificacion, color;

    public Nota(long id, String titulo, String nota, String image, String video, String audio, String fecha_creacion, String fecha_modificacion, String color) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
        this.image = image;
        this.video = video;
        this.audio = audio;
        this.fecha_creacion = fecha_creacion;
        this.fecha_modificacion = fecha_modificacion;
        this.color = color;
    }

    public Nota() {
        this(0, null, null, null, null, null, null, null, null);
    }

    protected Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        nota = in.readString();
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
        valores.put(ContratoBaseDatos.TablaNota.IMAGEN, this.getImage());
        valores.put(ContratoBaseDatos.TablaNota.VIDEO, this.getVideo());
        valores.put(ContratoBaseDatos.TablaNota.AUDIO, this.getAudio());
        valores.put(ContratoBaseDatos.TablaNota.FECHA_CREACION, this.getFecha_creacion());
        valores.put(ContratoBaseDatos.TablaNota.FECHA_MODIFICACION, this.getFecha_modificacion());
        valores.put(ContratoBaseDatos.TablaNota.COLOR, this.getColor());
        return valores;
    }

    public static Nota getNota(Cursor c) {
        Nota objeto = new Nota();
        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNota._ID)));
        objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.NOTA)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.IMAGEN)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.VIDEO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.AUDIO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA_CREACION)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA_MODIFICACION)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.COLOR)));
        return objeto;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static Creator<Nota> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nota='" + nota + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", fecha_modificacion='" + fecha_modificacion + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(nota);
        dest.writeString(image);
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(fecha_creacion);
        dest.writeString(fecha_modificacion);
        dest.writeString(color);
    }
}