package com.izv.dam.newquip.dialogo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TextView tvFechaRecordatorioDia;

    public DialogoFecha(View view) {
        tvFechaRecordatorioDia = (TextView) view;
    }

    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + month + "-" + dayOfMonth;
        tvFechaRecordatorioDia.setText(date);
    }
}
