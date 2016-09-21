package owlslubic.peptalkapp.views;

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
 * Created by owlslubic on 9/20/16.
 */

public class NewPepTalkWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "NewWidgetProvider";
    private static final String NEWPEPTALK = "newPepTalk";
    private RemoteViews mRemoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            //intent for onclick
            Intent intent = new Intent(context, NewPepTalkWidgetProvider.class);
            intent.setAction(NEWPEPTALK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_new_peptalk);
            mRemoteViews.setOnClickPendingIntent(R.id.widget_imageview, pendingIntent);

            //then update
            appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        if(intent.getAction().equals(NEWPEPTALK)){
            Intent intent1 = new Intent(context, AddPepTalkWidgetActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent1);
        }


        //update to reflect change
        ComponentName componentName = new ComponentName(context, NewPepTalkWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, mRemoteViews);
    }
}
