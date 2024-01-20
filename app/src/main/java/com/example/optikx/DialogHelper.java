package com.example.optikx;

import android.app.AlertDialog;
import android.content.Context;

import androidx.appcompat.view.ContextThemeWrapper;

public class DialogHelper {
    public static AlertDialog.Builder alertBuilder(Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.ShowAlertDialogTheme));
    }
}
