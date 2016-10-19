package com.izv.dam.newquip.dialogo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by dam on 18/10/16.
 */

public class DialogoHora extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TextView tvFechaRecordatorioHora;

    public DialogoHora(View view) {
        tvFechaRecordatorioHora = (TextView) view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        boolean is24Hour = true;


        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, is24Hour);
//        return new TimePickerDialog(getActivity(), this, hourOfDay, minute);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String date = hourOfDay + ":" + minute;
    }
}
