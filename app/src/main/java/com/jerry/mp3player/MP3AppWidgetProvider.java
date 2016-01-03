package com.jerry.mp3player;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This class is representing mp3 appWidget
 * Created by test on 29/12/15.
 */
public class MP3AppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "MP3_APPWIDGET_PROVIDER";

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive ");

        // receive the action by appWidget button clicking
        // noticing mp3Service so that it can respond for the clicking
        Intent musicControlIntent = new Intent(MP3Service.ACTION);
        context.sendBroadcast(musicControlIntent);
        super.onReceive(context, intent);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        Log.d(TAG, "onRestored");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");

        ComponentName thisWidget = new ComponentName(context, MP3AppWidgetProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            Intent serviceIntent = new Intent(context, MP3Service.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
            context.startService(serviceIntent);

    }
}
