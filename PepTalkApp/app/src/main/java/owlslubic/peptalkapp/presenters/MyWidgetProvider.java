package owlslubic.peptalkapp.presenters;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 9/5/16.
 */
public class MyWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_CLICK = "ACTION_CLICK";
    private static final String WIDGET_BUTTON = "owlslubic.peptalkapp.widget_button";
    RemoteViews mRemoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {


            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            mRemoteViews.setTextViewText(R.id.textview_widget, "emergency \npeptalk");

//            remoteViews.setOnClickPendingIntent(R.id.textview_widget,getPendingSelfIntent(context, ACTION_CLICK));
//            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
            Intent intent = new Intent(WIDGET_BUTTON);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.textview_widget, pendingIntent);


            //ok this is all good and fine, but i want to tap the widget and it will
            // change the text on that view to a peptalk set as default
            //like if peptalk.isWidgetDefault() set text to that one
            //like if dbref.child(pep).child(key).getValue(isWidgetDefault)==true

            //TODO set an on click listener for the widget which will set the textview to the peptalk default





            //dont think i need all this updating stuff cuz it's really just gonna be static

//            Intent intent = new Intent(context, MyWidgetProvider.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.textview_widget, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }

    }


//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//
//        if(WIDGET_BUTTON.equals(intent.getAction())){
//            mRemoteViews.setTextViewText(R.id.textview_widget,"change!");
//        }
//    }
//
//    protected PendingIntent getPendingSelfIntent(Context context, String action) {
//        Intent intent = new Intent(context, getClass());
//        intent.setAction(action);
//        return PendingIntent.getBroadcast(context, 0, intent, 0);
//    }
}
