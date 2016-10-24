package com.izv.dam.newquip.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilFecha;

public class AdaptadorNota extends CursorAdapter {
    TextView tv_titulo_nota, texo_nota, fecha_creacion, fecha_recordatorio;
    UtilFecha fecha_util;

    public AdaptadorNota(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /*
         * Cargamos los elementos que vamos a usar, id, titulo, texto
         */
        tv_titulo_nota = (TextView) view.findViewById(R.id.tvTituloNota);
        texo_nota = (TextView) view.findViewById(R.id.tvTexoNota);
        fecha_creacion = (TextView) view.findViewById(R.id.tvFecha);
        fecha_recordatorio = (TextView) view.findViewById(R.id.tvFechaRecordatorio);

        //Recogemos todos los datos del cursor de
        Nota nota = Nota.getNota(cursor);

        //Le damos los datos a los items del listView
        tv_titulo_nota.setText(nota.getTitulo());
        texo_nota.setText(nota.getNota());
        fecha_creacion.setText(fecha_util.cambiarFormato(nota.getFecha_creacion(), 1));

        if (nota.getFecha_recordatorio() != null){
            fecha_recordatorio.setText(fecha_util.cambiarFormato(nota.getFecha_recordatorio(), 1));
        } else {
            fecha_recordatorio.setText("");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }
}