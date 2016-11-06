package com.izv.dam.newquip.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_view_lista.setText((int) listaList.get(position).getId_lista());
        holder.text_view_lista.setText((int) listaList.get(position).getId_lista());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_view_nota, text_view_lista;

        public ViewHolder(View v) {
            super(v);
            text_view_nota = (TextView) v.findViewById(R.id.id_nota);
            text_view_lista = (TextView) v.findViewById(R.id.id_lista);
        }
    }


}