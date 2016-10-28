package owlslubic.peptalkapp.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.ChecklistActivity;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.RecyclerViewFrag;
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
//    public static final String USERS = "users";


    public static void setupNewFrag(String objectType, FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewFrag fragment = new NewFrag();
        Bundle args = new Bundle();
        args.putString(NEW_OR_EDIT, NEW);
        args.putString(OBJECT_TYPE, objectType);
        fragment.setArguments(args);

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
        NewFrag fragment = new NewFrag();
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


    public static void setupViewFrag(FragmentActivity activity, String objectType, String key, String title, String body) {
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
//        if (activity instanceof MainActivity) {
//            containerId = R.id.framelayout_main_frag_container;
//        } else
        //the above code is not necessary because if a frag is called from main, it's either NewFrag or RecyclerView Frag
        if (activity instanceof PepTalkListActivity) {
            containerId = R.id.peptalk_activity_frag_container;
        } else if (activity instanceof ChecklistActivity) {
            containerId = R.id.checklist_activity_frag_container;
        }
        transaction.add(containerId, fragment, VIEW_FRAG_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void setupMainActivityFrag(int id, FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (id == R.id.textview_main) {
            RecyclerViewFrag fragment = new RecyclerViewFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment, RECYCLERVIEW_FRAG_TAG);
        } else {
            NewFrag fragment = new NewFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment, NEW_FRAG_TAG);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void detachFragment(FragmentActivity activity, String tag) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment.getTag() != null) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction
                    //doing detach so that it doesn't have to reload the data............ right?
                    .detach(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commit();
        } else {
            Log.d(TAG, "detachFragment: OOPS, FRAG TAG WAS NULL");
        }

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


}
