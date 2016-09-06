package owlslubic.peptalkapp.presenters;

import owlslubic.peptalkapp.models.PepTalkObject;

/**
 * Created by owlslubic on 9/1/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position, PepTalkObject peptalk);
    //added object parameter to the above ebcause i'm trying to figure out how to tell it
    //which pep talk is in need of dismissing
    //if this works, i should make a parent class for my objects so that i can use this interface with both
}
