package owlslubic.peptalkapp.presenters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

public class ChecklistViewHolder extends RecyclerView.ViewHolder{
    CheckBox mCheckBox;
    TextView mItem;

    public ChecklistViewHolder(View itemView) {
        super(itemView);

        mItem = (TextView) itemView.findViewById(R.id.textview_checklist_item);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_checklist);

    }
    
}
