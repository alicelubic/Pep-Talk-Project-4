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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.presenters.ChecklistFirebaseAdapter;
import owlslubic.peptalkapp.presenters.ChecklistViewHolder;

public class ChecklistActivity extends AppCompatActivity {

    private static final String TAG = "ChecklistActivity";
    private static final String USERS = "users";
    private static final String CHECKLIST = "checklist";
    ChecklistFirebaseAdapter mFirebaseAdapter;
    private DatabaseReference mChecklistRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

//        getSupportActionBar().setTitle("Your Feel-Better Cheklist");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //recyclerview setup
        mChecklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_checklist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseAdapter = new ChecklistFirebaseAdapter(ChecklistItemObject.class, R.layout.card_checklist,
                ChecklistViewHolder.class, mChecklistRef,this);
        recyclerView.setAdapter(mFirebaseAdapter);

//
//        FirebaseRecyclerAdapter<ChecklistItemObject, ChecklistViewHolder> adapter =
//                new FirebaseRecyclerAdapter<ChecklistItemObject, ChecklistViewHolder>
//                        (ChecklistItemObject.class, R.layout.card_checklist, ChecklistViewHolder.class, dbRef.child("Checklist")) {
//                    @Override
//                    protected void populateViewHolder(ChecklistViewHolder holder, final ChecklistItemObject model, int position) {
//                        Log.i(TAG, "populateViewHolder: " + model.getText());
//                        holder.mItem.setText(model.getText());
//
//                        holder.mCard.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                CustomDialog.launchEditChecklistDialog(ChecklistActivity.this, model);
//                            }
//                        });
//
//                        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View view) {
//                               CustomDialog.launchDeleteChecklistDialog(model,ChecklistActivity.this);
//                                return true;
//                            }
//                        });
//                    }
//                };




        //fab for add new checklist item
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_checklist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.launchNewChecklistDialog(ChecklistActivity.this);
            }
        });


    }
}
