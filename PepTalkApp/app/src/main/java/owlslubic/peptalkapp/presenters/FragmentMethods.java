package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.views.ChecklistActivity;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;
import owlslubic.peptalkapp.views.fragments.NewEditFrag;
import owlslubic.peptalkapp.views.fragments.RecyclerFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;


/**
 * Created by owlslubic on 10/2/16.
 */

public class FragmentMethods {
    private static final String TAG = "FragmentMethods";
    public static final String RECYCLERVIEW_FRAG_TAG = "recyclerview_frag_tag";
    public static final String NEW_FRAG_TAG = "new_frag_tag";
    public static final String VIEW_FRAG_TAG = "view_frag_tag";
    public static final String TOP_TEXT = "top_text";
    public static final String BOTTOM_TEXT = "bottom_text";
    public static final String OBJECT_TYPE = "object_type";
    public static final String NEW_OR_EDIT = "new_or_edit";
    public static final String KEY = "key";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";
    public static final String PEPTALK_OBJ = "peptalk";
    public static final String CHECKLIST_OBJ = "checklist";
    public static final String WIDGET_FRAG_TAG = "widget_frag_tag";
    public static final String EMERGENCY_PEPTALK = "widget";

    public static void setupNewFrag(String objectType, FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewEditFrag fragment = new NewEditFrag();
        Bundle args = new Bundle();
        args.putString(NEW_OR_EDIT, NEW);
        args.putString(OBJECT_TYPE, objectType);
        fragment.setArguments(args);

        //determining fragment container based on the actiity that calls this method
        if (activity instanceof MainActivity) {
            transaction.add(R.id.framelayout_main_frag_container, fragment, NEW_FRAG_TAG);
        } else if (activity instanceof PepTalkListActivity) {
            transaction.add(R.id.peptalk_activity_frag_container, fragment, NEW_FRAG_TAG);
        } else if (activity instanceof ChecklistActivity) {
            transaction.add(R.id.checklist_activity_frag_container, fragment, NEW_FRAG_TAG);
        }

        transaction.addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void setupEditFrag(FragmentActivity activity, String objectType, String key, String title, String body) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        NewEditFrag fragment = new NewEditFrag();
        Bundle args = new Bundle();
        args.putString(OBJECT_TYPE, objectType);
        args.putString(NEW_OR_EDIT, EDIT);
        args.putString(KEY, key);
        args.putString(TOP_TEXT, title);
        args.putString(BOTTOM_TEXT, body);
        fragment.setArguments(args);

        int containerId = 0;

        if (objectType.equals(PEPTALK_OBJ)) {
            if (activity instanceof MainActivity) {
                containerId = R.id.framelayout_main_frag_container;
            } else if (activity instanceof PepTalkListActivity) {
                containerId = R.id.peptalk_activity_frag_container;
            } else {
                Log.d(TAG, "setupEditFrag: OBJECT TYPE PEPTALK -- FRAG CONTAINERS NULL");
            }
        } else if (objectType.equals(CHECKLIST_OBJ)) {
            containerId = R.id.checklist_activity_frag_container;
        } else {
            Log.d(TAG, "setupEditFrag: NO OBJECT TYPE?");
        }
        transaction.replace(containerId, fragment, NEW_FRAG_TAG)
                .addToBackStack(null)//what should this be if not null? idk?
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void setupViewFrag(FragmentActivity activity, String objectType,
                                     @Nullable String key, @Nullable String title, @Nullable String body) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        ViewFrag fragment = new ViewFrag();
        Bundle args = new Bundle();
        args.putString(OBJECT_TYPE, objectType);
        args.putString(NEW_OR_EDIT, VIEW);
        args.putString(KEY, key);
        args.putString(TOP_TEXT, title);
        args.putString(BOTTOM_TEXT, body);
        fragment.setArguments(args);

        int containerId = 0;
        String tag = VIEW_FRAG_TAG;

        if (activity instanceof MainActivity) {
            containerId = R.id.framelayout_main_frag_container;
            //must be emergency peptalk widget because no other use of ViewFrag in mainactivity
            tag = WIDGET_FRAG_TAG;
        } else if (activity instanceof PepTalkListActivity) {
            containerId = R.id.peptalk_activity_frag_container;
        } else if (activity instanceof ChecklistActivity) {
            containerId = R.id.checklist_activity_frag_container;
        }

        transaction.add(containerId, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void setupRecyclerFrag(int id, FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (id == R.id.textview_main) {
            RecyclerFrag fragment = new RecyclerFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment, RECYCLERVIEW_FRAG_TAG);
        } else {
            NewEditFrag fragment = new NewEditFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment, NEW_FRAG_TAG);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void detachFragment(FragmentActivity activity, String tag, View view) {//}, boolean hasChanged) {
//        if(!hasChanged) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment.getTag() != null) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            //TODO only detach if there hasn't been any changes to the text
            transaction
                    //doing detach so that it doesn't have to reload the data............ right?
                    .detach(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            Log.d(TAG, "detachFragment: OOPS, FRAG TAG WAS NULL");
        }
//        }else{
        //text has changed, so we don't wanna just detach without giving a warning
//        }

    }

    public static void addFragToBackStack(FragmentActivity activity, String tag) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment.getTag() != null) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction
                    //doing detach so that it doesn't have to reload the data............ right?
                    .addToBackStack(tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commit();
        } else {
            Log.d(TAG, "detachFragment: OOPS, FRAG TAG WAS NULL");
        }
    }

    public static void launchCancelAlertDialog(final Context context, final View view) {//}, final boolean hasTextChanged) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to navigate away?")
                .setPositiveButton("mhm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        detachFragment((FragmentActivity) context, NEW_FRAG_TAG, view);//, hasTextChanged);
                    }
                })
                .setNegativeButton("whoops!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
//                .setCancelable(true)
                .create();
        dialog.show();

    }

    public static boolean isFragVisible(FragmentActivity activity, String tag) {
        Fragment frag = activity.getSupportFragmentManager().findFragmentByTag(tag);
        return frag != null && frag.isVisible();
    }

}
