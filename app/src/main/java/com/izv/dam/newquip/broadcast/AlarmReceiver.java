package com.izv.dam.newquip.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;


import com.izv.dam.newquip.R;
import com.izv.dam.newquip.vistas.notas.VistaNota;



public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificaicon(context, "Titulo", "texto", "Alerta");
    }

    private void createNotificaicon(Context context, String msgTitle, String msgText, String msgAlert) {
        PendingIntent notifcIntent = PendingIntent.getActivity(context, 0, new Intent(context, VistaNota.class), 0);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(msgTitle)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.mipmap.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_add_note));
        nBuilder.setContentIntent(notifcIntent);
        nBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        nBuilder.setAutoCancel(true);
        NotificationManager nNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nNotificationManager.notify(1,nBuilder.build());



    }

}
