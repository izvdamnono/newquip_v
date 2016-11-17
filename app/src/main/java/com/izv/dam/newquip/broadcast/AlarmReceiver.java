package com.izv.dam.newquip.broadcast;

 import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.vistas.notas.VistaNota;

import java.io.File;
import java.util.Random;


public class AlarmReceiver extends BroadcastReceiver {

    private Nota nota;
    private String titulo_nota, texto_nota;
    public static final String BUNDLE_KEY = "nota";
    public static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        nota = intent.getExtras().getParcelable(BUNDLE_KEY);

        Intent intentVistaNota = new Intent(context, VistaNota.class);
        Bundle b = new Bundle();
        b.putParcelable(BUNDLE_KEY, nota);
        intentVistaNota.putExtras(b);

        titulo_nota = nota.getTitulo();
        texto_nota = nota.getNota();
        createNotificaicon(context, titulo_nota, texto_nota, "Alerta", intentVistaNota);
    }

    public void createNotificaicon(Context context, String msgTitle, String msgText, String msgAlert, Intent intentVistaNota) {

        System.out.println(intentVistaNota.getExtras().getParcelable(BUNDLE_KEY));

        PendingIntent notifcIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intentVistaNota, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(msgTitle)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.mipmap.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_add_note))
                .setContentIntent(notifcIntent)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true);



        Bundle b = intentVistaNota.getExtras();
        if (b != null) {
            nota = b.getParcelable(BUNDLE_KEY);
            System.out.println(nota.toString());

        }

        if (nota.getImagen() != null) {
            String imagenString = nota.getImagen();
            File imagenFile = new File(imagenString);
            Bitmap imagenBitmap = BitmapFactory.decodeFile(imagenFile.getAbsolutePath());

            nBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(imagenBitmap));
        }

        NotificationManager nNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nNotificationManager.notify(1, nBuilder.build());


    }

}
