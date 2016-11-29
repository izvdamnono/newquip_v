package com.izv.dam.newquip.dialogo;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private ComunicarActividadFragmentoFechaHora caf;

    public DialogoFecha() {
    }

    public Dialog onCreateDialog(Bundle saveInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        /*Formato: mié., 19 oct. 2016*/
        SimpleDateFormat formato = new SimpleDateFormat("E',' d MMM yyyy", new Locale("es", "ES"));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String fecha = formato.format(c.getTime());

        this.caf.setResultadoFecha(fecha);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        Log.v("Context", "Context");
        this.caf = (ComunicarActividadFragmentoFechaHora) context;
    }
}