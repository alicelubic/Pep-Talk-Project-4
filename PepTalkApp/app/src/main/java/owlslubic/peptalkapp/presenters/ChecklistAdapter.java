package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;

/**
 * Created by owlslubic on 8/30/16.
 */
public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistViewHolder> {
    Context mContext;
    List<ChecklistItemObject> mChecklistItems;//this list will be made up of String item = ChecklistItemObjects.getmText();

    public ChecklistAdapter(Context context, List<ChecklistItemObject> checklistItems) {
        mContext = context;
        mChecklistItems = checklistItems;
    }


    //this sets the layout for what each item in the recyclerview should use
    @Override
    public ChecklistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_checklist, parent, false);
        ChecklistViewHolder holder = new ChecklistViewHolder(view);
        return holder;
    }


    //this specifies the contents of each item in the recyclerview
    @Override
    public void onBindViewHolder(final ChecklistViewHolder holder, final int position) {
        //so i'll tell it to bind the checklist item to the cardview,
        holder.mItem.setText(mChecklistItems.get(position).getmText());
        //and set the onclick listener for the checkbox
        holder.mCheckBox.setChecked(false);
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click!", Toast.LENGTH_SHORT).show();
                //not sure if this works lol
                if (mChecklistItems.get(position).ismChecked() == true) {
                    holder.mCheckBox.setChecked(true);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return mChecklistItems.size();
    }
}
