package owlslubic.peptalkapp.presenters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.MyFragment;
import owlslubic.peptalkapp.views.PepTalkListActivity;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = "PepTalkViewHolder";
    public TextView mTitle;
    public CardView mCard;



    public PepTalkViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.textview_peptalk_title);
        mCard = (CardView) itemView.findViewById(R.id.cardview_peptalk);
        

    }


}




