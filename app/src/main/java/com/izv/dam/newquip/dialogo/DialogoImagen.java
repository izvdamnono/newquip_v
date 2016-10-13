package com.izv.dam.newquip.dialogo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Nota;


/*
 * Created by Nono on 11/10/2016.
 */

public class DialogoImagen extends DialogFragment {


    public DialogoImagen() {
    }

    public static DialogoImagen newInstance(Nota n) {
        DialogoImagen fragment = new DialogoImagen();
        Bundle args = new Bundle();
        args.putParcelable("nota", n);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Falta el ImagenView
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogImagen();
    }

    public AlertDialog createDialogImagen() {
        //String.format "%s %s" es el patron y getString(R.string.etiqueta_dialogo_borrar) son
        // las concatenaciones
        String titulo_dialogo = getString(R.string.etiqueta_dialogo_imagen);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo_dialogo);
        builder.setMessage(R.string.etiqueta_dialogo_imagen);

        //Positive para la foto de la camara
        builder.setPositiveButton(R.string.etiqueta_dialogo_foto, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //Negative para la imagen de la galeria
        builder.setNegativeButton(R.string.etiqueta_dialogo_galeria, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertImagen = builder.create();
        return alertImagen;
    }


}
