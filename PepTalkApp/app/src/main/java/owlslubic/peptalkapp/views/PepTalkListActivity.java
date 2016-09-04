package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.OnStartDragListener;
import owlslubic.peptalkapp.presenters.PepTalkFirebaseAdapter;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;
import owlslubic.peptalkapp.presenters.SimpleItemTouchHelperCallback;

public class PepTalkListActivity extends AppCompatActivity implements OnStartDragListener {

    private static final String TAG = "PepTalkListActivity";
    private DatabaseReference mDbRef;
    private PepTalkFirebaseAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pep_talk_list);

        getSupportActionBar().setTitle("Your Pep Talks");


        //fab launches dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_peptalk_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.launchNewPeptalkDialog(PepTalkListActivity.this);
            }
        });

        mDbRef = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> adapter =
//                new FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder>
//                        (PepTalkObject.class, R.layout.card_peptalk, PepTalkViewHolder.class, dbRef.child("PepTalks")) {
//                    @Override
//                    protected void populateViewHolder(PepTalkViewHolder holder, final PepTalkObject model, int position) {
//                        Log.i(TAG, "populateViewHolder: "+ model.getTitle());
//                        holder.mTitle.setText(model.getTitle());
//                        holder.mCard.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //launches edit pep talk dialog
//                                CustomDialog.launchEditPeptalkDialog(PepTalkListActivity.this, model);
//                            }
//                        });
//                        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View view) {
//                                //launches delete peptalk with are you sure? dialog
//                                CustomDialog.launchDeletePepTalkDialog(model, PepTalkListActivity.this);
//                                return true;
//                            }
//                        });
//                    }
//                };

        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class,
                R.layout.card_peptalk, PepTalkViewHolder.class, mDbRef.child("PepTalks"),
                this, this);
        recyclerView.setAdapter(mFirebaseAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);



        //TODO inflate menu that has back home button, and maybe some sort of info overflow menu.. do i need it?
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
