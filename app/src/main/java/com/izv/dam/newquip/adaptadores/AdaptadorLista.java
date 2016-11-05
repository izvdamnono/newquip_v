package com.izv.dam.newquip.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.izv.dam.newquip.R;

/**
 * Created by nono on 5/11/16.
 */


public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> {
    private String[] mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }


    public AdaptadorLista(String[] myDataset) {
        mDataset = myDataset;
    }


    @Override
    public AdaptadorLista.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;

    }
}