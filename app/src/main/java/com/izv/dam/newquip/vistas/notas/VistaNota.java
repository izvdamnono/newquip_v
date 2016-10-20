package com.izv.dam.newquip.vistas.notas;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.dialogo.DialogoFecha;
import com.izv.dam.newquip.dialogo.DialogoHora;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilFecha;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {

    private EditText editTextTitulo, editTextNota;
    private TextView tvFechaRecordatorioDia, tvFechaRecordatorioHora;
    private ImageButton id_imageButton;
    private Button btn_img;
    private ImageView img_view;
    private Nota nota = new Nota();
    private PresentadorNota presentador;
    private static final int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        init();

        ejecutar();

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
            }
        }
        mostrarNota(nota);
    }

    private void init() {

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        //Boton
        btn_img = (Button) findViewById(R.id.id_imagen_btn);
        //Imagen
        img_view = (ImageView) findViewById(R.id.id_imagen);

        // Fechas de recordatorio
        // DIA: jue. 1 sept 2016
        // HORA: 12:52:30
        tvFechaRecordatorioDia = (TextView) findViewById(R.id.tvFechaRecordatorioDia);
        tvFechaRecordatorioHora = (TextView) findViewById(R.id.tvFechaRecordatorioHora);
        presentador = new PresentadorNota(this);
    }

    private void ejecutar() {
        tvFechaRecordatorioHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoHora dialog = new DialogoHora(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "Hora Recordatorio");
            }
        });
        tvFechaRecordatorioDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoFecha dialogFecha = new DialogoFecha(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogFecha.show(ft, "Fecha Recordatorio");
            }
        });


        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCamaraGaleria();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nota, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.alert:


                String fecha_recordatorio = tvFechaRecordatorioDia.getText().toString() + " " + tvFechaRecordatorioHora.getText().toString();
                String nuevo_formato = UtilFecha.cambiarFormato(fecha_recordatorio, 0);


                saveRecordatorio(nuevo_formato);
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.save:
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                saveNota();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    /*
     * Ejemplos de guardar y restaurar la actividad
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
        /*
         * Ejemplo
            savedInstanceState.putBoolean("MyBoolean", true);
            savedInstanceState.putDouble("myDouble", 1.9);
            savedInstanceState.putInt("MyInt", 1);
            savedInstanceState.putString("MyString", "Welcome back to Android");
         */
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*
         * Ejemplos
            boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
            double myDouble = savedInstanceState.getDouble("myDouble");
            int myInt = savedInstanceState.getInt("MyInt");
            String myString = savedInstanceState.getString("MyString");
        */

    }


    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
        String formato_a_cortar = nota.getFecha_recordatorio();

        if (formato_a_cortar != null) {
            formato_a_cortar = UtilFecha.cambiarFormato(formato_a_cortar, 1);
            String[] fecha_recordatorio = UtilFecha.cortarFormato(formato_a_cortar);
            tvFechaRecordatorioDia.setText(fecha_recordatorio[0]);
            tvFechaRecordatorioHora.setText(fecha_recordatorio[1]);
        }
    }

    /*
     * Con este metodo se guarda la nota
     */

    private void saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());

        SimpleDateFormat formato_fecha_actual = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", new Locale("es", "ES"));
        String fecha_actual = formato_fecha_actual.format(new Date());

        //Si no tiene fecha de creacion se la da
        if (nota.getFecha_creacion() == null) {
            nota.setFecha_creacion(fecha_actual);
        }
        //Fecha de modificacion se la cambia por la actual
        nota.setFecha_modificacion(fecha_actual);


        long r = presentador.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0) {
            nota.setId(r);
        }
    }

    private void saveRecordatorio(String fecha_recordatorio) {
        nota.setFecha_recordatorio(fecha_recordatorio);
    }

    /*
     * Metodos con los que se abre el selector de imagenes de la galeria
     */
    public void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), SELECT_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        Uri selectedImage;

        String filePath = null;
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();

                    if (requestCode == SELECT_FILE) {

                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                        Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                        // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                        img_view.setImageBitmap(bmp);

                    }
                }
                break;
        }
    }


    /*
     *
     */
    public void mostrarDialogoCamaraGaleria() {
        /*
            DialogoImagen fragmentImagen = DialogoImagen.newInstance(n, img_view);
            fragmentImagen.show(getSupportFragmentManager(), "Dialogo Imagen");
         */
        final CharSequence[] items = {"Sacar Foto", "Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opcion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        //Camara

                        break;
                    case 1:
                        //Galeria
                        abrirGaleria();
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}