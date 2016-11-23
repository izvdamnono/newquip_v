package com.izv.dam.newquip.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.vistas.notas.VistaNota;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Unchicodelavida on 23/11/2016.
 */

public class UtilImagen {

    public static String getRealPath(Uri datos, Context context) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(datos);
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /*
     * Metodos con los que se abre el selector de imagenes de la galeria
     */
    public static void abrirGaleria(Activity activity, Context context, int SELECT_FILE) {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.selecciona_una_imagen)), SELECT_FILE);
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
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    public static String takePicture(Activity activity, Context context, int IMAGE_CAPTURE) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri contentUri = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        intent.putExtra("data", contentUri);

        activity.startActivityForResult(intent, IMAGE_CAPTURE);
        System.out.println("takePicture" + contentUri.toString());
        return contentUri.toString();
    }

    /*
     * Este metodo añade a la galeria las fotos que se hacen en la aplicacion
     */
    public static String galleryAddPic(Activity activity, Context context, String pathFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(pathFile));
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
        System.out.println("galleryAddPic" + contentUri.toString());
        return contentUri.toString();
    }


}
