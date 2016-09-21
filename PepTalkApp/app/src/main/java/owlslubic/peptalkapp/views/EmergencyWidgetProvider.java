package owlslubic.peptalkapp.views;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 9/5/16.
 */
public class EmergencyWidgetProvider extends AppWidgetProvider {
    private static final String EMERGENCY_PEPTALK = "emergency_peptalk";
    private static final String WIDGET_VISIBLE = "widget_visible";
    private static final String TAG = "EmergencyWidgetProvider";
    private static final String UNDO = "undo";
    private static final String PREFS = "prefs";
    RemoteViews mRemoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //to get all widgets belonging to this provider
        for (int appWidgetId : appWidgetIds) {

            //intent for onclick
            Intent intent = new Intent(context, EmergencyWidgetProvider.class);
            intent.setAction(EMERGENCY_PEPTALK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            //intent for undo, setting it back to "emergency peptalk"
            Intent intent2 = new Intent(context, EmergencyWidgetProvider.class);
            intent2.setAction(UNDO);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0);

            //layout and attach onclicklistener
            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_emergency);
            mRemoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
//            mRemoteViews.setOnClickPendingIntent(R.id.textview_widget_reset, pendingIntent2);
            mRemoteViews.setOnClickPendingIntent(R.id.reset_button_widget, pendingIntent2);
            //then update i guess
            appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String text = prefs.getString("WIDGET_TEXT", "Add your own pep talk from the app!");
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_emergency);


        if (intent.getAction().equals(EMERGENCY_PEPTALK)) {
            mRemoteViews.setTextViewText(R.id.textview_widget, text);
            mRemoteViews.setViewVisibility(R.id.reset_button_widget, View.VISIBLE);

        }
        else if(intent.getAction().equals(UNDO)){
            mRemoteViews.setTextViewText(R.id.textview_widget, context.getString(R.string.emergency_pep_talk));
            mRemoteViews.setViewVisibility(R.id.reset_button_widget, View.INVISIBLE);
        }


        //update to reflect change
        ComponentName componentName = new ComponentName(context, EmergencyWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, mRemoteViews);

    }

}
