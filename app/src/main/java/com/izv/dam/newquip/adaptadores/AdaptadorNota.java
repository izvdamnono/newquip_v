package com.izv.dam.newquip.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ClickListener;
import com.izv.dam.newquip.contrato.ClickListenerLong;
import com.izv.dam.newquip.databinding.ItemBinding;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilFecha;

public class AdaptadorNota extends RecyclerView.Adapter<AdaptadorNota.ViewHolder> {

    private ClickListener mItemClickListener;
    private ClickListenerLong mItemClickListenerLong;
    private Cursor mCursor;
    private ItemBinding binding;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        /*
        TextView tv_titulo_nota, texo_nota, fecha_creacion, fecha_recordatorio;
        UtilFecha fecha_util;
        */
        ClickListener mItemClickListener;
        ClickListenerLong mItemClickListenerLong;

        ItemBinding binding;

        public ViewHolder(View itemView, ClickListener myItemClickListener, ClickListenerLong myItemClickListenerLong) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            /*
            tv_titulo_nota = (TextView)itemView.findViewById(R.id.tvTituloNota);
            texo_nota = (TextView)itemView.findViewById(R.id.tvTexoNota);
            fecha_creacion = (TextView)itemView.findViewById(R.id.tvFecha);
            fecha_recordatorio = (TextView)itemView.findViewById(R.id.tvFechaRecordatorio);
            */
            this.mItemClickListener=myItemClickListener;
            this.mItemClickListenerLong=myItemClickListenerLong;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);



        }
        public ItemBinding getBinding() {
            return binding;
        }
        /*
        public void bindNota(String titulo,String texo,String creacion,String recordatorio) {
            tv_titulo_nota.setText(titulo);
            texo_nota.setText(texo);
            fecha_creacion.setText(creacion);
            fecha_recordatorio.setText(fecha_util.cambiarFormato("2016-12-12", 1));

        }*/

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View arg0) {
            if(mItemClickListenerLong != null){
                mItemClickListenerLong.onItemLongClick(arg0, getPosition());
            }
            return true;
        }
    }



    public AdaptadorNota(Cursor c) {
        mCursor = c;
    }

    @Override
    public AdaptadorNota.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        ViewHolder tvh = new ViewHolder(itemView,mItemClickListener,mItemClickListenerLong);

        return tvh;
    }

    @Override
    public void onBindViewHolder(AdaptadorNota.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Nota nota = Nota.getNota(mCursor);
        /*
        String sTitulo= nota.getTitulo();
        String sTexo = nota.getNota();
        String sCreacion = nota.getFecha_creacion();
        String sRecordatorio = nota.getFecha_recordatorio();
        */
        holder.getBinding().setVariable(BR.nota,nota);
        holder.getBinding().executePendingBindings();

        //holder.bindNota(sTitulo,sTexo,sCreacion,sRecordatorio);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public Cursor changeCursor(Cursor c){

        if(mCursor==c){
            return null;
        }
        this.mCursor=c;
        if (c!=null){
            this.notifyDataSetChanged();
        }
        mCursor = c;

        return mCursor;

    }


    public void setOnItemClickListener(ClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(ClickListenerLong listener){
        this.mItemClickListenerLong = listener;
    }



    /*
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Cargamos los elementos que vamos a usar, id, titulo, texto

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
    */

}