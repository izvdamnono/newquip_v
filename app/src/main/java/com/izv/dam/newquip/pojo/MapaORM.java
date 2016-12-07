package com.izv.dam.newquip.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Unchicodelavida on 07/12/2016.
 */

@DatabaseTable(tableName = "accounts")
public class MapaORM {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(id = true)
    private String historia;
    @DatabaseField(canBeNull = false)
    private String latitud;
    @DatabaseField(canBeNull = false)
    private String longitud;

    MapaORM() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public MapaORM(int id, String historia, String latitud, String longitud) {
        this.id = id;
        this.historia = historia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        String sb = "id=" + id +
                ", " + "str=" + historia +
                ", " + "str=" + latitud +
                ", " + "str=" + longitud;
        return sb;
    }


}
