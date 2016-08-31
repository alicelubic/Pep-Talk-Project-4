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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.PepTalkAdapter;
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
                Toast.makeText(PepTalkListActivity.this, "new pep talk comin' your way!", Toast.LENGTH_SHORT).show();
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
                    protected void populateViewHolder(PepTalkViewHolder holder, PepTalkObject model, int position) {
                        Log.i(TAG, "populateViewHolder: "+ model.getTitle());
                        holder.mTitle.setText(model.getTitle());
                    }
                };

        recyclerView.setAdapter(adapter);



        //dummy data - when using recyclerview from firebase i won't need a list, ha!
//        ArrayList<PepTalkObject> peptalks = new ArrayList<>();
//        peptalks.add(new PepTalkObject("For when you're feelin' like a failure","You're not!",false));
//        peptalks.add(new PepTalkObject("Go have a quick cry in the bathroom","You're not!",false));
//        peptalks.add(new PepTalkObject("It's not the end of the world!","You're not!",false));
//        peptalks.add(new PepTalkObject("you're okAY!","You're not!"));



        //recyclerview for the cardviews that each display a pep talk title
        //if there is no title, then it should display the first x characters of the body + "..."
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        PepTalkAdapter adapter = new PepTalkAdapter(this, peptalks);
//        recyclerView.setAdapter(adapter);



        //TODO inflate menu that has back home button, and maybe some sort of info overflow menu.. do i need it?

    }
}
