package com.sam_chordas.android.stockhawk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

    public static void showToastInUiThread(final Context ctx,
                                           final int stringRes) {

        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, ctx.getString(stringRes), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
