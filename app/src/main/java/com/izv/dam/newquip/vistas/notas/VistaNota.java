package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izv.dam.newquip.broadcast.AlarmReceiver;
import com.izv.dam.newquip.vistas.notification.Notificacion;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.dialogo.DialogoFecha;
import com.izv.dam.newquip.dialogo.DialogoHora;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilFecha;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {

    Toolbar toolbar;
    EditText editTextTitulo, editTextNota;
    TextView tvFechaRecordatorioDia, tvFechaRecordatorioHora;

    Button btn_img;
    ImageView img_view;

    private Nota nota = new Nota();
    private PresentadorNota presentador;
    private static final int SELECT_FILE = 0;
    private static final int IMAGE_CAPTURE = 1;
    Uri file;


    private static String temp_file_path;

    NotificationManager notification_manager;
    boolean is_notific_active = false;
    private int notifID = 33;
    public static final String BUNDLE_KEY = "nota";

    private RecyclerView recycler_view_lista;
    private RecyclerView.Adapter recycler_view_adapter;
    private RecyclerView.LayoutManager recycler_view_layout_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nota);

        init();

        ejecutar();

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable(BUNDLE_KEY);
        } else {

            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable(BUNDLE_KEY);

            }
        }
        mostrarNota(nota);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        //Boton
        btn_img = (Button) findViewById(R.id.id_imagen_btn);
        //Imagen
        img_view = (ImageView) findViewById(R.id.id_imagen);


        recycler_view_lista = (RecyclerView) findViewById(R.id.id_recycler_view_lista);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler_view_lista.setHasFixedSize(true);

        // use a linear layout manager
        recycler_view_layout_manager = new LinearLayoutManager(this);
        recycler_view_lista.setLayoutManager(recycler_view_layout_manager);

        // specify an adapter (see also next example)
        recycler_view_adapter = new MyAdapter(myDataset);
        recycler_view_lista.setAdapter(recycler_view_adapter);


        // Fechas de recordatorio
        // DIA: jue. 1 sept 2016
        // HORA: 12:52:30
        tvFechaRecordatorioDia = (TextView) findViewById(R.id.tvFechaRecordatorioDia);
        tvFechaRecordatorioHora = (TextView) findViewById(R.id.tvFechaRecordatorioHora);
        presentador = new PresentadorNota(this);
    }

    private void ejecutar() {

        tvFechaRecordatorioDia.setText(UtilFecha.fechaHoyDia());
        tvFechaRecordatorioHora.setText(UtilFecha.fechaHoyHora());

        tvFechaRecordatorioDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoFecha dialogFecha = new DialogoFecha(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogFecha.show(ft, "Fecha Recordatorio");
            }
        });

        tvFechaRecordatorioHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoHora dialog = new DialogoHora(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "Hora Recordatorio");
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btn_img.setEnabled(false);
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCamaraGaleria(v);
//                showNotification();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nota, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String fecha_recordatorio, nuevo_formato;
        switch (item.getItemId()) {
            case R.id.delete_alert:
                saveRecordatorio(null);
                stopNotification();
                Toast.makeText(this, "Alert Delete", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ok_alert:
                fecha_recordatorio = tvFechaRecordatorioDia.getText().toString() + " " + tvFechaRecordatorioHora.getText().toString();
                nuevo_formato = UtilFecha.cambiarFormato(fecha_recordatorio, 0);

                addAlarmNotification(nuevo_formato);
                saveRecordatorio(nuevo_formato);
                Toast.makeText(this, "Alert " + nuevo_formato, Toast.LENGTH_SHORT).show();
                return true;
/*
            case R.id.delete:
                saveNota();
                Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
                return true;
*/
            case R.id.save:
//                View v = findViewById(R.id.id_activity_detail_nota);
//                showNotification(v);
                saveNota();
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
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
        outState.putParcelable(BUNDLE_KEY, nota);
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
        if (nota.getImagen() != null) {
            Bitmap bMap = BitmapFactory.decodeFile(nota.getImagen());
            img_view.setImageBitmap(bMap);
        }
    }

    /*
     * Con este metodo se guarda la nota
     */
    private void saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());

        SimpleDateFormat formato_fecha_actual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
        String fecha_actual = formato_fecha_actual.format(new Date());

        //Si no tiene fecha de creacion se la da
        if (nota.getFecha_creacion() == null) {
            nota.setFecha_creacion(fecha_actual);
        }
        //Fecha de modificacion se la cambia por la actual
        nota.setFecha_modificacion(fecha_actual);
        if (nota.getImagen() == null) {
            nota.setImagen(temp_file_path);
        }

        long r = presentador.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0) {
            nota.setId(r);
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
        String wholeID = DocumentsContract.getDocumentId(datos); // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA}; // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = VistaNota.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    /*
     * Dialogo de la camara y la galeria
     */
    public void mostrarDialogoCamaraGaleria(final View v) {
        /*
            DialogoImagen fragmentImagen = DialogoImagen.newInstance(n, img_view);
            fragmentImagen.show(getSupportFragmentManager(), "Dialogo Imagen");
         */
        final CharSequence[] items = {"Galeria", "Cámara"};
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);
        alert_builder.setTitle("Elige una opcion");
        alert_builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    //Galeria
                    case SELECT_FILE:
                        Toast.makeText(VistaNota.this, "Galeria", Toast.LENGTH_SHORT).show();
                        abrirGaleria();
                        break;
                    //camara
                    case IMAGE_CAPTURE:
                        Toast.makeText(VistaNota.this, "Cámara", Toast.LENGTH_SHORT).show();
                        takePicture();
                        break;

                }
            }
        });
        AlertDialog alert = alert_builder.create();
        alert.show();
    }

     /*
     * Se ejecuta cada vez que seleccionamos una imagen desde la galerio o desde la cámara
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case SELECT_FILE://Funciona
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    temp_file_path = getRealPath(selectedImage);
                    System.out.println("1" + temp_file_path);

                    setPic(temp_file_path);
                    saveImagen(temp_file_path);
                }
                break;
            case IMAGE_CAPTURE://No funciona
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("2" + temp_file_path);

                    galleryAddPic(temp_file_path);//Añade la imagen a la galeria
                    setPic(temp_file_path);
                    saveImagen(temp_file_path);
                }
                break;
        }
    }

    /*
     * Metodos con los que se abre el selector de imagenes de la galeria
     */
    public void abrirGaleria() {
        Intent intent = new Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selecciona_una_imagen)), SELECT_FILE);
    }

    /*
     * Camara
     */
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "NewQuipPictures");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File f = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        temp_file_path = f.toString();
        System.out.println("FILE getOutputMediaFile: " + f.toString());
        return f;
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        intent.putExtra("data", file);

        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    private void galleryAddPic(String pathFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(pathFile));
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic(String filePathAddGallery) {
        // Get the dimensions of the View
        int targetW = img_view.getWidth();
        int targetH = img_view.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePathAddGallery, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePathAddGallery, bmOptions);
        img_view.setImageBitmap(bitmap);
    }


    /*
     * Notificaciones
     */
    public void showNotification() {
        NotificationCompat.Builder notificBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Notificacion prueba")
                .setContentText("Texto prueba")
                .setTicker("Alarma de prueba")
                .setSmallIcon(R.mipmap.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_add_note));
        Intent intentNotification = new Intent(this, Notificacion.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(Notificacion.class);
        taskStackBuilder.addNextIntent(intentNotification);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificBuilder.setAutoCancel(true);//Permite que se borre cuando abrimos la notificacion
        notificBuilder.setContentIntent(pendingIntent);

        notification_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification_manager.notify(notifID, notificBuilder.build());
        is_notific_active = true;
    }

    public void stopNotification() {

        if (is_notific_active) {
            notification_manager.cancel(notifID);
        }
    }

    public void addAlarmNotification(String alarm) {
        Long date_alarm = UtilFecha.stringToLongTime(alarm);
        GregorianCalendar alertTime = new GregorianCalendar();
        alertTime.setTimeInMillis(date_alarm);

        Log.v("Alarm", alarm);
        Log.v("Alarm getTimeInMillis", Long.toString(date_alarm));

        Intent alertIntent = new Intent(this, AlarmReceiver.class);

        Bundle b = new Bundle();
        b.putParcelable(BUNDLE_KEY, nota);
        alertIntent.putExtras(b);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                alertTime.getTimeInMillis(),
                PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        );

    }

    /*
     * Permisos
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btn_img.setEnabled(true);
            }
        }
    }

}