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

public class AdaptadorNota extends CursorAdapter {
    TextView tv_titulo_nota, texo_nota, fecha_creacion;

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

        //Recogemos todos los datos del cursor de
        Nota nota = Nota.getNota(cursor);

        //Le damos los datos a los items del listView
        tv_titulo_nota.setText(nota.getTitulo());
        texo_nota.setText(nota.getNota());
        fecha_creacion.setText(nota.getFecha_creacion());
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