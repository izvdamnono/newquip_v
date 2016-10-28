package com.izv.dam.newquip.util;

/**
 * Created by dam on 28/10/16.
 */

public class UtilCadenas {
    public static String getCondicionesSql(String condicionesIni, String newCondiciones) {
        return getCondicionesSql(condicionesIni, newCondiciones, "and");
    }

    public static String getCondicionesSql(String condicionesIni, String newCondiciones, String conector) {
        if (condicionesIni == null || condicionesIni.trim().length() == 0) {
            return newCondiciones;
        }
        return condicionesIni + " " + conector + " " + newCondiciones;
    }

    public static String[] getNewArray(String[] arrayInicial, String parametro) {
        String[] newArray;
        if (arrayInicial == null) {
            return new String[]{parametro};
        }
        newArray = new String[arrayInicial.length + 1];
        for (int i = 0; i < arrayInicial.length; i++) {
            newArray[i] = arrayInicial[i];
        }
        newArray[arrayInicial.length] = parametro;
        return newArray;
    }

}
