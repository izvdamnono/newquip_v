package com.izv.dam.newquip.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.vistas.notas.VistaNota;


public class AlarmReceiver extends BroadcastReceiver {

    private Nota nota;
    private String titulo_nota, texto_nota;


    @Override
    public void onReceive(Context context, Intent intent) {
        nota = intent.getExtras().getParcelable("nota");

        Intent intentVistaNota = new Intent(context, VistaNota.class);
        Bundle b = new Bundle();
        b.putParcelable("nota", nota);
        intentVistaNota.putExtras(b);


        titulo_nota = nota.getTitulo();
        texto_nota = nota.getNota();
        createNotificaicon(context, titulo_nota, texto_nota, "Alerta", intentVistaNota);
    }

    private void createNotificaicon(Context context, String msgTitle, String msgText, String msgAlert, Intent intentVistaNota) {

        PendingIntent notifcIntent = PendingIntent.getActivity(context, 0, intentVistaNota, 0);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(msgTitle)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.mipmap.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_add_note));
        nBuilder.setContentIntent(notifcIntent);
        nBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        nBuilder.setAutoCancel(true);


        NotificationManager nNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nNotificationManager.notify(1, nBuilder.build());


    }

}
