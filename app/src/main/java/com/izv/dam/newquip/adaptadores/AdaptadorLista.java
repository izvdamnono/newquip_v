package com.izv.dam.newquip.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Lista;

import java.util.List;


public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> {

    Context context;
    List<Lista> listaList;

    public AdaptadorLista(Context context, List<Lista> listaList) {
        this.context = context;
        this.listaList = listaList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id_lista = "" + (listaList.get(position).getId_lista());
        String id_nota = "" + (listaList.get(position).getId_nota());
        String lista_texto = listaList.get(position).getTexto_lista();
        boolean check_box_lista = listaList.get(position).isHecho();

        holder.text_view_id_lista.setText("id_lista: " + id_lista);
        holder.text_view_id_nota.setText("id_nota: " + id_nota);
        holder.text_view_lista_texto.setText(lista_texto);
        holder.check_box_lista.setChecked(check_box_lista);
    }

    @Override
    public int getItemCount() {
        return this.listaList.size();
    }


    public void addLista() {
        listaList.add(new Lista());
        notifyItemInserted(listaList.size());
    }

    public void deleteUltimaLista() {
        int position = listaList.size();

        if (position > 0) {
            listaList.remove(position-1);
            notifyItemRemoved(position-1);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox check_box_lista;
        TextView text_view_id_nota, text_view_id_lista;
        EditText text_view_lista_texto;

        public ViewHolder(View v) {
            super(v);
            check_box_lista = (CheckBox) v.findViewById(R.id.check_box_lista);
            text_view_id_nota = (TextView) v.findViewById(R.id.id_nota);
            text_view_id_lista = (TextView) v.findViewById(R.id.id_lista);
            text_view_lista_texto = (EditText) v.findViewById(R.id.id_texto_lista);

            check_box_lista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Lista lista = listaList.get(position);
                    lista.setHecho(check_box_lista.isChecked());
                }
            });

            text_view_lista_texto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (s.length() > 0) {
                        Lista lista = listaList.get(position);
                        lista.setTexto_lista(String.valueOf(text_view_lista_texto.getText()));
                    }
                }
            });


        }
    }


}