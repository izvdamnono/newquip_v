package com.izv.dam.newquip.dialogo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TimePicker;

/**
 * Created by dam on 18/10/16.
 */


@SuppressLint("ValidFragment")
public class DialogoHora extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private ComunicarActividadFragmentoFechaHora caf;

    public DialogoHora() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int minute = calendar.get(Calendar.MINUTE);
        boolean is24Hour = true;

        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, is24Hour);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String fecha = hourOfDay + ":" + minute + ":00";
        this.caf.setResultadoHora(fecha);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        Log.v("Context", "Context");
        this.caf = (ComunicarActividadFragmentoFechaHora) context;
    }
}
