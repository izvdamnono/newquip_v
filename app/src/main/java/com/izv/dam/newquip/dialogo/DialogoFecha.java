package com.izv.dam.newquip.dialogo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TextView tvFechaRecordatorioDia;

    public DialogoFecha(View view) {
        tvFechaRecordatorioDia = (TextView) view;
    }


    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Formato: mié., 19 oct. 2016
        SimpleDateFormat formato = new SimpleDateFormat("E',' d MMM yyyy", new Locale("es", "ES"));
        String fecha = formato.format(new Date(year-1900, month, dayOfMonth));
        tvFechaRecordatorioDia.setText(fecha);
    }

}
