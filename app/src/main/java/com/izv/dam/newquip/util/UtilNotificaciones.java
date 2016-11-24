package com.izv.dam.newquip.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.izv.dam.newquip.R;

/**
 * Created by Unchicodelavida on 24/11/2016.
 */

public class UtilNotificaciones {
    public static void snackBarEdit(Activity activity, Context context, String text) {
        View view = activity.getCurrentFocus();
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.primary));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.show();
    }
}
