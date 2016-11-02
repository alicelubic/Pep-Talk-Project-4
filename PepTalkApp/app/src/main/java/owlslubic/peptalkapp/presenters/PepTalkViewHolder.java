package owlslubic.peptalkapp.presenters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = "PepTalkViewHolder";
    public TextView mTitle, mFragTitle, mFragBody;
    public CardView mCard;

    public CardView mFragCard;
    public ImageButton mEdit, mTrash;



    public PepTalkViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.textview_peptalk_title);
        mCard = (CardView) itemView.findViewById(R.id.cardview_peptalk);

        mFragCard = (CardView) itemView.findViewById(R.id.cardview_fragment);
        mFragBody = (TextView) itemView.findViewById(R.id.textview_frag_body);
        mFragTitle = (TextView) itemView.findViewById(R.id.textview_frag_title);
        mEdit = (ImageButton) itemView.findViewById(R.id.button_frag_edit_peptalk);
        mTrash = (ImageButton) itemView.findViewById(R.id.imagebutton_frag_view_delete);



    }


}




