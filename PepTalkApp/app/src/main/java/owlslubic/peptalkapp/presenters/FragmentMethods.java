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

import static owlslubic.peptalkapp.views.fragments.NewFrag.CHECKLIST;
import static owlslubic.peptalkapp.views.fragments.NewFrag.KEY;
import static owlslubic.peptalkapp.views.fragments.NewFrag.PEPTALKS;

/**
 * Created by owlslubic on 10/2/16.
 */

public class FragmentMethods {
    private static final String TAG = "FragmentMethods";
    public static final String RECYCLERVIEW_FRAG_TAG = "recyclerview_frag_tag";
    public static final String NEW_FRAG_TAG = "new_frag_tag";
    public static final String VIEW_FRAG_TAG = "view_frag_tag";


    public static void setupNewFrag(String objectType, FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewFrag fragment = new NewFrag();
        Bundle args = new Bundle();
        args.putString(NewFrag.NEW_OR_EDIT, NewFrag.NEW);
        args.putString(NewFrag.OBJECT_TYPE, objectType);
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

    public static void addFragToBackStack(FragmentActivity activity, String tag){
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


    public static void setupEditFrag(FragmentActivity activity, String objectType, String key, String title, String body) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        NewFrag fragment = new NewFrag();
        Bundle args = new Bundle();
        args.putString(NewFrag.OBJECT_TYPE, objectType);
        args.putString(NewFrag.NEW_OR_EDIT, NewFrag.EDIT);
        args.putString(KEY, key);
        args.putString(NewFrag.TOP_TEXT, title);
        args.putString(NewFrag.BOTTOM_TEXT, body);
        fragment.setArguments(args);

        int containerId = 0;

        if (objectType.equals(PEPTALKS)) {
            if (activity instanceof MainActivity) {
                containerId = R.id.framelayout_main_frag_container;
            } else if (activity instanceof PepTalkListActivity) {
                containerId = R.id.peptalk_activity_frag_container;
            } else {
                Log.d(TAG, "setupEditFrag: OBJECT TYPE PEPTALK -- FRAG CONTAINERS NULL");
            }
        } else if (objectType.equals(CHECKLIST)) {
            containerId = R.id.checklist_activity_frag_container;
        } else {
            Log.d(TAG, "setupEditFrag: NO OBJECT TYPE?");
        }
        transaction.replace(containerId, fragment, NEW_FRAG_TAG)//do i need to put a tag here?
                .addToBackStack(null)//what should this be if not null? idk?
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    public static void setupViewFrag(FragmentActivity activity, String objectType, String key, String title, String body) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        ViewFrag fragment = new ViewFrag();
        Bundle args = new Bundle();
        args.putString(NewFrag.OBJECT_TYPE, objectType);
        args.putString(NewFrag.NEW_OR_EDIT, ViewFrag.VIEW);
        args.putString(NewFrag.KEY, key);
        args.putString(NewFrag.TOP_TEXT, title);
        args.putString(NewFrag.BOTTOM_TEXT, body);
        fragment.setArguments(args);

        int containerId = 0;
        if (activity instanceof MainActivity) {//would that be recyclerview frag?
            containerId = R.id.framelayout_main_frag_container;
        } else if (activity instanceof PepTalkListActivity) {
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
            transaction.add(R.id.framelayout_main_frag_container, fragment, FragmentMethods.RECYCLERVIEW_FRAG_TAG);
        } else {
            NewFrag fragment = new NewFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment, FragmentMethods.NEW_FRAG_TAG);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


}
