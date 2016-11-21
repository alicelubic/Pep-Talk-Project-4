package owlslubic.peptalkapp.presenters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

public class ChecklistViewHolder extends RecyclerView.ViewHolder {
    CheckBox mCheckBox;
    TextView mItem;
    CardView mCard;

    public ChecklistViewHolder(View itemView) {
        super(itemView);

        mItem = (TextView) itemView.findViewById(R.id.textview_checklist_item);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_checklist);
        mCard = (CardView) itemView.findViewById(R.id.cardview_checklist);
    }

}
