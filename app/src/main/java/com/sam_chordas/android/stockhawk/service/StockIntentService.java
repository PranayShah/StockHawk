package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockDetailFragment;
import com.sam_chordas.android.stockhawk.StocksAppWidgetProvider;
import com.sam_chordas.android.stockhawk.ToastUtils;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService implements StockDetailFragment.OnFragmentInteractionListener{
  private final String TAG = StockIntentService.class.getName();
  public StockIntentService(){
    super(StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }

  @Override protected void onHandleIntent(Intent intent) {
    StockTaskService stockTaskService = new StockTaskService(this);
    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.
    int result = stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
    if (result== GcmNetworkManager.RESULT_FAILURE)
    {
      ToastUtils.showToastInUiThread(getApplicationContext(), R.string.stock_not_found);
    }
      else
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(getPackageName(), StocksAppWidgetProvider.class.getSimpleName())), R.id.stack_view);
    }
  }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
