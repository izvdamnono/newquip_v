package com.izv.dam.newquip.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Unchicodelavida on 07/12/2016.
 */

@DatabaseTable(tableName = "MapaORM")
public class MapaORM {
    @DatabaseField(columnName = "id_nota")
    int id_nota;
    @DatabaseField(columnName = "historia")
    private String historia;
    @DatabaseField(canBeNull = false, columnName = "latitud")
    private String latitud;
    @DatabaseField(canBeNull = false, columnName = "longitud")
    private String longitud;

    MapaORM() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public int getId_nota() {
        return id_nota;
    }

    public void setId_nota(int id_nota) {
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

    public MapaORM(int id_nota, String historia, String latitud, String longitud) {
        this.id_nota = id_nota;
        this.historia = historia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "id_nota=" + id_nota +
                ", " + "str=" + historia +
                ", " + "str=" + latitud +
                ", " + "str=" + longitud;
    }


}
