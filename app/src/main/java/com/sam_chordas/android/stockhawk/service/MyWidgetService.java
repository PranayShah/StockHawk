package com.sam_chordas.android.stockhawk.service;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.rest.StackRemoteViewsFactory;

/**
 * Created by Pranay on 04/04/2016.
 */
public class MyWidgetService extends RemoteViewsService {
    public static final String TAG = MyWidgetService.class.getSimpleName();
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(TAG, "onGetViewFactory: ");
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
