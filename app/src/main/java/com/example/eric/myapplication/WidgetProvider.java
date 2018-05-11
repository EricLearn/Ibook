package com.example.eric.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.widget.RemoteViews;

/**
 * Created by Eric on 2018/3/12.
 */

public class WidgetProvider extends AppWidgetProvider {

    private final String ACTION_UPDATE_ALL = "com.example.eric.widget.UPDATE_ALL";
    private final String FOLD_WIDGET = "com.example.eric.widget.FOLD_WIDGET";

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.module_widget_layout);
        Intent intent = new Intent(FOLD_WIDGET);
        //Intent intent = new Intent(context,module_activity_widget.class);
        //intent.addCategory(FOLD_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.id.flodButton, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.flodButton, pendingIntent);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    /**
     *  接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();

        if (action.equals(ACTION_UPDATE_ALL)) {
            // 注册点击事件
        }
        else if (action.equals(FOLD_WIDGET)) {
            // 自定义点击
            //Intent new_intent = new Intent(context,module_activity_widget.class);
        }
    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
