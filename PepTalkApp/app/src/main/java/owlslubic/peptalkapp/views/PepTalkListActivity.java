package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;

public class PepTalkListActivity extends AppCompatActivity {

    private static final String TAG = "PepTalkListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pep_talk_list);

        //fab launches dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_peptalk_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.launchNewPeptalkDialog(PepTalkListActivity.this);
            }
        });

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> adapter =
                new FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder>
                        (PepTalkObject.class, R.layout.card_peptalk, PepTalkViewHolder.class, dbRef.child("PepTalks")) {
                    @Override
                    protected void populateViewHolder(PepTalkViewHolder holder, final PepTalkObject model, int position) {
                        Log.i(TAG, "populateViewHolder: "+ model.getTitle());
                        holder.mTitle.setText(model.getTitle());
                        holder.mCard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //launches edit pep talk dialog
                                CustomDialog.launchEditPeptalkDialog(PepTalkListActivity.this, model);
                            }
                        });
                        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                //launches delete peptalk with are you sure? dialog
                                CustomDialog.launchDeletePepTalkDialog(model, PepTalkListActivity.this);
                                return true;
                            }
                        });
                    }
                };

        recyclerView.setAdapter(adapter);



        //TODO inflate menu that has back home button, and maybe some sort of info overflow menu.. do i need it?

    }
}
