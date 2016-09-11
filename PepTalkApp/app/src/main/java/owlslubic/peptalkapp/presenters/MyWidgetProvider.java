package owlslubic.peptalkapp.presenters;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 9/5/16.
 */
public class MyWidgetProvider extends AppWidgetProvider {
    private static final String EMERGENCY_PEPTALK = "emergency_peptalk";
    private static final String TAG = "MyWidgetProvider";
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

            //layout and attach onclicklistener
            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            mRemoteViews.setOnClickPendingIntent(R.id.textview_widget, pendingIntent);

            //then update i guess
            appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);

        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(EMERGENCY_PEPTALK)) {
            Toast.makeText(context, "Yay it works!", Toast.LENGTH_SHORT).show();
//            mRemoteViews.setTextViewText(R.id.textview_widget, "here it is!");

        }

    }

}
