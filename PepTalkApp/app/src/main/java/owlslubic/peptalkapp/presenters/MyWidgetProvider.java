package owlslubic.peptalkapp.presenters;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.views.MainActivity;

/**
 * Created by owlslubic on 9/5/16.
 */
public class MyWidgetProvider extends AppWidgetProvider {
    private static final String EMERGENCY_PEPTALK = "emergency_peptalk";
    private static final String TAG = "MyWidgetProvider";
    private static final String UNDO = "undo";
    RemoteViews mRemoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //to get all widgets belonging to this provider
        final int N = appWidgetIds.length;
        for (int appWidgetId : appWidgetIds) {

            //intent for onclick
            Intent intent = new Intent(context, MyWidgetProvider.class);
            intent.setAction(EMERGENCY_PEPTALK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            //intent for undo, setting it back to "emergency peptalk"
            Intent intent2 = new Intent(context, MyWidgetProvider.class);
            intent2.setAction(UNDO);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0);

            //layout and attach onclicklistener
            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            mRemoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
            mRemoteViews.setOnClickPendingIntent(R.id.textview_widget_reset, pendingIntent2);

            //then update i guess
            appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        SharedPreferences prefs = context.getSharedPreferences("PEP_PREFS", 0);
        String text = prefs.getString("WIDGET_TEXT", "oops");
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        if (intent.getAction().equals(EMERGENCY_PEPTALK)) {
            mRemoteViews.setTextViewText(R.id.textview_widget, text);
            mRemoteViews.setViewVisibility(R.id.textview_widget_reset, View.VISIBLE);
        }
        else if(intent.getAction().equals(UNDO)){
            mRemoteViews.setTextViewText(R.id.textview_widget, context.getString(R.string.emergency_pep_talk));
            mRemoteViews.setViewVisibility(R.id.textview_widget_reset, View.INVISIBLE);

        }

        //update to reflect change
        ComponentName componentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, mRemoteViews);

    }

}
