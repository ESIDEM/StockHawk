package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;

import static android.content.Intent.URI_INTENT_SCHEME;

/**
 * Created by ESIDEM jnr on 5/24/2017.
 */

public class StockWidgetProvider extends AppWidgetProvider{

    public static final String TAG = StockWidgetProvider.class.getName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent widgetIntent = new Intent(context, StockWidgetService.class);
            context.startService(widgetIntent);
            remoteViews.setRemoteAdapter(R.id.lv_widget, widgetIntent);

            // intent to launch MainActivity
            Intent intentThatStartsMain = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intentThatStartsMain, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

            // intent to launch DetailActivity
            Intent intentThatStartsDetail = new Intent(context, StockDetailsActivity.class);
            intentThatStartsDetail.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intentThatStartsDetail.setData(Uri.parse(intentThatStartsDetail.toUri(URI_INTENT_SCHEME)));
            PendingIntent pendingIntentDetail = PendingIntent.getActivity(
                    context, 0, intentThatStartsDetail, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.lv_widget, pendingIntentDetail);


            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null) {
            String intentAction = intent.getAction();
            Log.d(TAG, "onReceive got action " + intentAction);
            if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intentAction)) {
            }
        }
    }

}
