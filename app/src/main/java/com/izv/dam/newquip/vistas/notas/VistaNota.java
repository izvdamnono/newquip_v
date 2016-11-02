package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

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
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    Uri file;

    private static String filePathAddGallery;

    NotificationManager notificationManager;
    boolean isNotificActive = false;
    private int notifID = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nota);

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
//                mostrarDialogoCamaraGaleria(v);
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

            case R.id.save:
//                View v = findViewById(R.id.id_activity_detail_nota);
//                showNotification(v);
                saveNota();
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.delete:
                saveRecordatorio(null);
                saveNota();
                Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
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

        SimpleDateFormat formato_fecha_actual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
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
     * Dialogo de la camara y la galeria
     */
    public void mostrarDialogoCamaraGaleria(final View v) {
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
                    //camara
                    case 0:
                        Toast.makeText(VistaNota.this, "Cámara", Toast.LENGTH_SHORT).show();
                        takePicture(v);
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

    /*
     * Metodos con los que se abre el selector de imagenes de la galeria
     */
    public void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), SELECT_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();

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
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    galleryAddPic(filePathAddGallery);
                    setPic();
                }

                break;
        }

    }


    /*
     * Camara
     */
    @Nullable
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "NewQuipPictures");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File f = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        filePathAddGallery = f.toString();
        //    Log.v("FILE: ", f.toString());
        return f;
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        intent.putExtra("data", file);

        startActivityForResult(intent, 2);
    }

    private void galleryAddPic(String pathFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(pathFile));
        // Log.v("ruta", filePathAddGallery);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
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

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifID, notificBuilder.build());
        isNotificActive = true;
    }

    public void stopNotification() {

        if (isNotificActive) {
            notificationManager.cancel(notifID);
        }
    }

    public void addAlarmNotification(String alarm) {
        Long date_alarm = UtilFecha.stringToLongTime(alarm);
        GregorianCalendar alertTime = new GregorianCalendar();
        alertTime.setTimeInMillis(date_alarm);

        Log.v("Alarm", alarm);
        Log.v("Alarm getTimeInMillis", Long.toString(date_alarm));

        Intent alertIntent = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                alertTime.getTimeInMillis(),
                PendingIntent.getBroadcast(
                        this,
                        1,
                        alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
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