package com.udacity.stockhawk.util;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.widget.StockWidgetProvider;

/**
 * Created by ESIDEM jnr on 5/24/2017.
 */

public final class WidgetUpdater {

    public synchronized static void update(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, StockWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);

    }
}
