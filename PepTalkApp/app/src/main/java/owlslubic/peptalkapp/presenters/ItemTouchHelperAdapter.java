package owlslubic.peptalkapp.presenters;

/**
 * Created by owlslubic on 9/1/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
