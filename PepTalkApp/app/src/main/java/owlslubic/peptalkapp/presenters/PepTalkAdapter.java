package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkAdapter extends RecyclerView.Adapter<PepTalkViewHolder> {
    Context mContext;
    List<PepTalkObject> mPepTalkTitles;

    public PepTalkAdapter(Context context, List<PepTalkObject> pepTalkTitles) {
        mContext = context;
        this.mPepTalkTitles = pepTalkTitles;
    }

    //this sets the layout for what each item in the recyclerview should use
    @Override
    public PepTalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_peptalk,parent,false);
        PepTalkViewHolder holder = new PepTalkViewHolder(view);
        return holder;
    }

    //this specifies the contents of each item in the recyclerview
    @Override
    public void onBindViewHolder(PepTalkViewHolder holder, int position) {
        //here you'll set an onLONGclicklistener for the cardview that will launch the edit dialog for it
        //onclick should let you view the pep talk, so if my fragment business is bad news, just make a lil alert dialog that'll display it...... that's fine
        holder.mTitle.setText(mPepTalkTitles.get(position).getTitle());
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "card click!", Toast.LENGTH_SHORT).show();
                //this will launch pep talk view... frag or dialog or whatever it ends up being
            }
        });
        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //this will launch edit dialog
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPepTalkTitles.size();
    }
}
