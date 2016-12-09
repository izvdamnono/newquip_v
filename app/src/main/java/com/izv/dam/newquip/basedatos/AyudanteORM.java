package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.izv.dam.newquip.pojo.MapaORM;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by dam on 9/12/16.
 */

public class AyudanteORM extends OrmLiteSqliteOpenHelper {
    public final static int VERSION = 1;
    private Dao<MapaORM, Integer> simpleDao = null;
    private RuntimeExceptionDao<MapaORM, Integer> simpleRuntimeDao = null;


    public AyudanteORM(Context context) {
        super(context, "MapaORM", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MapaORM.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, MapaORM.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        public Dao<MapaORM, Integer> getDato(){

            return getRuntimeExceptionDao(MapaORM.class);
        }
    */
    public Dao<MapaORM, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(MapaORM.class);
        }
        return simpleDao;
    }

    public RuntimeExceptionDao<MapaORM, Integer> getDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(MapaORM.class);
        }
        return simpleRuntimeDao;
    }


    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }
}
