package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorLista;
import com.izv.dam.newquip.broadcast.AlarmReceiver;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.dialogo.DialogoFecha;
import com.izv.dam.newquip.dialogo.DialogoHora;
import com.izv.dam.newquip.gestion.GestionLista;
import com.izv.dam.newquip.pojo.Lista;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.CompartirPDFFile;
import com.izv.dam.newquip.util.GeneratePDFFile;
import com.izv.dam.newquip.util.UtilFecha;
import com.squareup.picasso.Picasso;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {
    private ActionBar actionBar;
    private Menu menu;
    private EditText editTextTitulo, editTextNota;
    private TextView tvFechaRecordatorioDia, tvFechaRecordatorioHora;
    private LinearLayout bottom_sheet;
    private BottomSheetBehavior bsb;
    private ImageView img_view;
    private View scroll_view;
    private Nota nota = new Nota();
    private PresentadorNota presentadorNota;
    private static final int SELECT_FILE = 0;
    private static final int IMAGE_CAPTURE = 1;
    /*Ruta temporal para guardar la imagen*/
    private static final String PDFS = "PDFGenerados";
    private static String temp_file_path = null;

    NotificationManager notification_manager;
    boolean is_notific_active = false;
    private static final int notifID = 1;
    public static final String BUNDLE_KEY = "nota";
    //    public static final String BUNDLE_KEY_LISTAS = "listas";
    private RecyclerView mRecyclerView;
    private List<Lista> listaList = new ArrayList<>();
    private AdaptadorLista adaptadorLista;
    private ImageButton add_list, add_alarm;
    /*------ PDF ------*/
    private static final String Directory_NewQuipPDF = "PDFGenerados";
    private static final String Directory_NewQuipPicture = "NewQuipPictures";

    RelativeLayout relativeLayout;
    private ImageButton add_delete_imagen;
    private ImageButton add_color;
    /*------ PICASSO ------*/
    private static final int widthImg = 600, heightImg = 600;
    /*------ COLOR PICKER DIALOG ------*/
    private int mSelectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_nota);
        init();
        if (savedInstanceState != null)
            nota = savedInstanceState.getParcelable(BUNDLE_KEY);
        else {
            Bundle b = getIntent().getExtras();
            if (b != null)
                nota = b.getParcelable(BUNDLE_KEY);
        }
        mostrarNota(nota);
        ejecutar();
//        if(getResources().getBoolean(R.bool.landscape_only)){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
    }

    private void init() {
        bottom_sheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Nota ");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        scroll_view = findViewById(R.id.id_scroll_view);
        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        /*CONTROL IMAGEN*/
        add_delete_imagen = (ImageButton) findViewById(R.id.anadir_imagen);
        /*CONTROL DIALOGO COLOR*/
        add_color = (ImageButton) findViewById(R.id.id_palette);
        /*CONTROL DIALOGO ALARMA*/
        add_alarm = (ImageButton) findViewById(R.id.id_add_alarm);
        /*Imagen*/
        img_view = (ImageView) findViewById(R.id.id_imagen);
        /*------ RECYCLER VIEW ------*/
        add_list = (ImageButton) findViewById(R.id.id_add_lista);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_view_listas);
        /*-----------*/
        tvFechaRecordatorioDia = (TextView) findViewById(R.id.tvFechaRecordatorioDia);
        tvFechaRecordatorioHora = (TextView) findViewById(R.id.tvFechaRecordatorioHora);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_nota_relativeLayout);
        presentadorNota = new PresentadorNota(this);
    }

    private void ejecutar() {
        bsb = BottomSheetBehavior.from(bottom_sheet);
        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
        tvFechaRecordatorioDia.setText(UtilFecha.fechaHoyDia());
        tvFechaRecordatorioHora.setText(UtilFecha.fechaHoyHora());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            add_delete_imagen.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        /*------ RECYCLER VIEW ------*/
        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNota.requestFocus();
                adaptadorLista.addLista();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        cargarDatosLista();
        adaptadorLista = new AdaptadorLista(this, listaList);
        mRecyclerView.setAdapter(adaptadorLista);
        /*-----------*/
        actionBar.setSubtitle(nota.getTitulo());
        bottomSheetFunction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_nota, menu);
        if (nota.getFecha_recordatorio() == null)
            menu.getItem(1).setIcon(R.mipmap.ic_ok_alert);
        else menu.getItem(1).setIcon(R.mipmap.ic_delete_alert);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String fecha_recordatorio, nuevo_formato;
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.id_share:
                /* GenerarPDF */
                compartirNotaPDF();
                return true;
            case R.id.ok_alert:
                saveNota();
                if (nota.getFecha_recordatorio() == null) {
                    menu.getItem(1).setIcon(R.mipmap.ic_delete_alert);
                    saveNota();
                    fecha_recordatorio = tvFechaRecordatorioDia.getText().toString() + " " + tvFechaRecordatorioHora.getText().toString();
                    nuevo_formato = UtilFecha.cambiarFormato(fecha_recordatorio, 0);
                    addAlarmNotification(nuevo_formato);
                    saveRecordatorio(nuevo_formato);
                } else {
                    menu.getItem(1).setIcon(R.mipmap.ic_ok_alert);
                    saveRecordatorio(null);
                    stopNotification();
                    snackBarEdit("Recordatorio borrado");
                }
                return true;
            case R.id.save:
                saveNota();
                snackBarEdit("Nota guardada");
                return true;
            case R.id.bottom_sheet_item:
                if (bsb.getState() == BottomSheetBehavior.STATE_HIDDEN || bsb.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                else if (bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED || bsb.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        saveNota();
        presentadorNota.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentadorNota.onResume();
        super.onResume();
    }

    /* Ejemplos de guardar y restaurar la actividad */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_KEY, nota);
        outState.putString("Dia", tvFechaRecordatorioDia.getText().toString());
        outState.putString("Hora", tvFechaRecordatorioHora.getText().toString());
        super.onSaveInstanceState(outState);
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
        tvFechaRecordatorioDia.setText(savedInstanceState.getString("Dia"));
        tvFechaRecordatorioHora.setText(savedInstanceState.getString("Hora"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
        String formato_a_cortar = nota.getFecha_recordatorio();
        if (nota.getColor() != null)
            scroll_view.setBackgroundColor(Color.parseColor(nota.getColor()));
        if (formato_a_cortar != null) {
            formato_a_cortar = UtilFecha.cambiarFormato(formato_a_cortar, 1);
            String[] fecha_recordatorio = UtilFecha.cortarFormato(formato_a_cortar);
            tvFechaRecordatorioDia.setText(fecha_recordatorio[0]);
            tvFechaRecordatorioHora.setText(fecha_recordatorio[1]);
        }
        if (nota.getImagen() != null)
            setPic(nota.getImagen());
    }

    @Override
    public void mostrarListas(ArrayList<Lista> ls) {
        long id_nota = nota.getId();
        if (id_nota != 0) {
            GestionLista gestionLista = new GestionLista(this);
            ArrayList<Lista> listas = gestionLista.getListas(id_nota);
            if (listas != null)
                listaList = listas;
        }
    }

    /* Funcion que consulta la base de datos en busca de listas que tenga el id de la nota a mostrar */
    public void cargarDatosLista() {
        mostrarListas(null);
    }

    /* Con este metodo se guarda la nota */
    private void saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());
        SimpleDateFormat formato_fecha_actual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String fecha_actual = formato_fecha_actual.format(new Date());
        /*Si no tiene fecha de creacion se la da*/
        if (nota.getFecha_creacion() == null)
            nota.setFecha_creacion(fecha_actual);
        nota.setFecha_modificacion(fecha_actual);
        long r = presentadorNota.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0)
            nota.setId(r);
        saveLista();
    }

    private void saveLista() {
        if (listaList.size() > 0) {
            long id_lista, id_nota;
            String texto_lista;
            boolean hecho;
            for (Lista lista : listaList) {
                id_lista = lista.getId_lista();
                id_nota = nota.getId();
                texto_lista = lista.getTexto_lista();
                hecho = lista.isHecho();
                VistaNota.this.presentadorNota.onSaveLista(new Lista(id_lista, id_nota, texto_lista, hecho));
            }
        }
    }

    private void saveRecordatorio(String fecha_recordatorio) {
        nota.setFecha_recordatorio(fecha_recordatorio);
    }

    private void saveImagen(String imagen) {
        nota.setImagen(imagen);
    }

    private String getRealPath(Uri datos) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(datos);
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = VistaNota.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst())
                filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    /* Dialogo de la camara y la galeria */
    public void mostrarDialogoCamaraGaleria() {
        final CharSequence[] items = {getString(R.string.galeria), getString(R.string.camara)};
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);
        alert_builder.setTitle(R.string.opcion);
        alert_builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    /*Galeria*/
                    case SELECT_FILE:
                        abrirGaleria();
                        break;
                    /*Camara*/
                    case IMAGE_CAPTURE:
                        takePicture();
                        break;
                }
            }
        });
        AlertDialog alert = alert_builder.create();
        alert.show();
    }

    /* Se ejecuta cada vez que seleccionamos una imagen desde la galerio o desde la cámara */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*Funciona*/
            case SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    temp_file_path = getRealPath(selectedImage);
                    setPic(temp_file_path);
                    saveImagen(temp_file_path);
                }
                break;
            /*Funciona*/
            case IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    galleryAddPic(temp_file_path);
                    /*Añade la imagen a la galeria*/
                    setPic(temp_file_path);
                    saveImagen(temp_file_path);
                }
                break;
        }
    }

    /* Metodos con los que se abre el selector de imagenes de la galeria */
    public void abrirGaleria() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selecciona_una_imagen)), SELECT_FILE);
    }

    /* Camara */
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Directory_NewQuipPicture);
        if (!mediaStorageDir.exists())
            if (!mediaStorageDir.mkdirs())
                return null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File f = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        temp_file_path = f.toString();
        return f;
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        intent.putExtra("data", file);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    /* Este metodo añade a la galeria las fotos que se hacen en la aplicacion */
    private void galleryAddPic(String pathFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(pathFile));
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic(String filePathAddGallery) {
        Picasso.with(this).load(new File(filePathAddGallery)).resize(widthImg, heightImg).into(img_view);
    }

    public void deleteListaAndFocus(Lista l) {
        presentadorNota.onDeleteLista(l);
        editTextNota.requestFocus();
    }

    /* Notificaciones */
    public void stopNotification() {
        if (is_notific_active)
            notification_manager.cancel(notifID);
    }

    public void addAlarmNotification(String alarm) {
        Long date_alarm = UtilFecha.stringToLongTime(alarm);
        GregorianCalendar alertTime = new GregorianCalendar();
        snackBarEdit(getString(R.string.recordatorio_) + alarm);
        alertTime.setTimeInMillis(date_alarm);
        Intent alertIntent = new Intent(this, AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putParcelable(BUNDLE_KEY, nota);
        alertIntent.putExtras(b);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime.getTimeInMillis(), PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    /* Permisos */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                add_delete_imagen.setEnabled(true);
    }

    public void generarAsyncTaskPDF() {
        editTextNota = (EditText) findViewById(R.id.etNota);
        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        String textoNota = editTextNota.getText().toString();
        String textoTitulo = editTextTitulo.getText().toString();
        String imagen = nota.getImagen();
        Context contexto = getApplicationContext();
        String extension = ".pdf";
        String NOMBRE_PDF = UtilFecha.formatDate(Calendar.getInstance().getTime()) + extension;
        String nombre = NOMBRE_PDF.replace(":", "-");
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File DIRECTORIO_PDF = new File(tarjetaSD + File.separator + PDFS);
        if (!DIRECTORIO_PDF.exists()) {
            DIRECTORIO_PDF.mkdir();
        }
        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + PDFS + File.separator + nombre;
        GeneratePDFFile crearPDF = new GeneratePDFFile(textoTitulo, textoNota, imagen, nombre_completo, contexto);
        crearPDF.execute();
        //crearPDF.mostrarPDF(nombre_completo, this);
    }

    public void crearYCompartirPDF() {
        editTextNota = (EditText) findViewById(R.id.etNota);
        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        String textoNota = editTextNota.getText().toString();
        String textoTitulo = editTextTitulo.getText().toString();
        String imagen = nota.getImagen();
        Context contexto = getApplicationContext();
        String extension = ".pdf";
        String NOMBRE_PDF = UtilFecha.formatDate(Calendar.getInstance().getTime()) + extension;
        String nombre = NOMBRE_PDF.replace(":", "-");
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File DIRECTORIO_PDF = new File(tarjetaSD + File.separator + PDFS);
        if (!DIRECTORIO_PDF.exists()) {
            DIRECTORIO_PDF.mkdir();
        }
        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + PDFS + File.separator + nombre;
        CompartirPDFFile crearPDF = new CompartirPDFFile(textoTitulo, textoNota, imagen, nombre_completo, contexto);
        crearPDF.execute();
        //crearPDF.mostrarPDF(nombre_completo, this);
    }

    public void compartirNotaPDF() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.elegir_opcion);
        builder.setItems(R.array.opcionesPDF, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0://CREAR PDF
                        generarAsyncTaskPDF();
                        break;
                    case 1://COMPARTIR CON EL RESTO DE APPS
                        crearYCompartirPDF();
                        break;
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void bottomSheetFunction() {
        add_delete_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_view.getDrawable() == null)
                    mostrarDialogoCamaraGaleria();
                else {
                    img_view.setImageResource(0);
                    saveImagen(null);
                    saveNota();
                }
            }
        });
        add_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Añadimos un color a la nota*/
                Context contexto = getApplicationContext();
                mSelectedColor = ContextCompat.getColor(contexto, R.color.flamingo);
                final int[] mColors = getResources().getIntArray(R.array.paletteNewQuip);
                ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title, mColors, mSelectedColor, 5, ColorPickerDialog.SIZE_SMALL);
                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        String hex = Integer.toHexString(color);
                        scroll_view.setBackgroundColor(color);
                        nota.setColor("#" + hex);
                    }
                });
                dialog.show(getFragmentManager(), getString(R.string.color_text));
            }
        });
        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Añadimos listas*/
                editTextNota.requestFocus();
                adaptadorLista.addLista();
            }
        });
        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Creamos un dialogo de seleccion para el picker*/
                String fecha = getString(R.string.dia_) + tvFechaRecordatorioDia.getText().toString();
                String hora = getString(R.string.hora_) + tvFechaRecordatorioHora.getText().toString();
                final CharSequence[] items = {fecha, hora};
                AlertDialog.Builder alert_builder = new AlertDialog.Builder(VistaNota.this);
                alert_builder.setTitle(R.string.edit_alarm);
                alert_builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        FragmentTransaction ft;
                        switch (item) {
                            case 0:
                                DialogoFecha dialogFecha = new DialogoFecha(tvFechaRecordatorioDia);
                                ft = getFragmentManager().beginTransaction();
                                dialogFecha.show(ft, getString(R.string.dia_recordatorio));
                                break;
                            case 1:
                                DialogoHora dialogHora = new DialogoHora(tvFechaRecordatorioHora);
                                ft = getFragmentManager().beginTransaction();
                                dialogHora.show(ft, getString(R.string.hora_recordatorio));
                                break;
                        }
                    }
                });
                AlertDialog alert = alert_builder.create();
                alert.show();
            }
        });
    }

    public void snackBarEdit(String text){
        View v = getCurrentFocus();
        if (v != null) {
            Snackbar snackbar;
            snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.white));
            snackbar.show();
        }
    }
}