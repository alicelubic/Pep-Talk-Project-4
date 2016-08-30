package owlslubic.peptalkapp.presenters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkViewHolder extends RecyclerView.ViewHolder{
    TextView mTitle;
    CardView mCard;


    public PepTalkViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.textview_peptalk_title);
        mCard = (CardView) itemView.findViewById(R.id.cardview_peptalk);
    }



}


