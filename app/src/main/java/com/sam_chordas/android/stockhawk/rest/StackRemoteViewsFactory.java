package com.sam_chordas.android.stockhawk.rest;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Pranay on 04/04/2016.
 */
public class StackRemoteViewsFactory  implements RemoteViewsService.RemoteViewsFactory{
    private final String TAG = StackRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    boolean isConnected;
    private int mAppWidgetId;
    Cursor c;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // The intent service is for executing immediate pulls from the Yahoo API
        // GCMTaskService can only schedule tasks, they cannot execute immediately

        if (isConnected){
            Log.i(TAG, "onCreate: ");
            /*CursorLoader mCursorLoader = new CursorLoader(mContext, QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);
            mCursorLoader.registerListener(CURSOR_LOADER_ID, this);
            mCursorLoader.startLoading();*/

            c = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);
            assert c != null;
            Log.i(TAG, "onCreate: " + String.valueOf(c.moveToFirst()));
        } else{
            Log.i(TAG, "onCreate: failed");
        }

    }

    @Override
    public void onDataSetChanged() {
        // Revert back to our process' identity so we can work with our
        // content provider
        final long identityToken = Binder.clearCallingIdentity();
        c = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
        assert c != null;
        Log.i(TAG, "onCreate dataSetChanged: " + String.valueOf(c.moveToFirst()));
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
// Stop the cursor loader
        c.close();
    }

    @Override
    public int getCount() {
        return c.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.i(TAG, "getViewAt: " + String.valueOf(position));
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        Log.i(TAG, "getViewAt: "+ String.valueOf(c.moveToPosition(position)));
        Log.i(TAG, "getViewAt: symbol"+ c.getString(c.getColumnIndex("symbol")));
        rv.setTextViewText(R.id.stock_symbol, c.getString(c.getColumnIndex("symbol")));
        rv.setTextViewText(R.id.bid_price, c.getString(c.getColumnIndex("bid_price")));
        rv.setTextViewText(R.id.change, c.getString(c.getColumnIndex("percent_change")));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
