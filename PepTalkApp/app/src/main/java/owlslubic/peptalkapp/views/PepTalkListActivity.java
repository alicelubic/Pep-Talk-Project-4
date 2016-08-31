package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.PepTalkAdapter;

public class PepTalkListActivity extends AppCompatActivity {

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


        //dummy data - when using recyclerview from firebase i won't need a list, ha!
        ArrayList<PepTalkObject> peptalks = new ArrayList<>();
        peptalks.add(new PepTalkObject("For when you're feelin' like a failure","You're not!",false));
        peptalks.add(new PepTalkObject("Go have a quick cry in the bathroom","You're not!",false));
        peptalks.add(new PepTalkObject("It's not the end of the world!","You're not!",false));
        peptalks.add(new PepTalkObject("you're okAY!","You're not!",false));



        //recyclerview for the cardviews that each display a pep talk title
        //if there is no title, then it should display the first x characters of the body + "..."
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        PepTalkAdapter adapter = new PepTalkAdapter(this, peptalks);
        recyclerView.setAdapter(adapter);



        //TODO inflate menu that has back home button, and maybe some sort of info overflow menu.. do i need it?

/*
        //firebase setup - where will this live? here? dialog? its own data model class?
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        //object reference
        DatabaseReference pepParentObjRef = dbRef.child("peptalks");//the children of this object are the individual peptalks
        DatabaseReference pepChild1 = pepParentObjRef.child("peptalk1");

        //write to da db
        //push puts values inside an object
        //pepChild1.push().setValue(editText.getText().toString())


        pepParentObjRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //dont have to specify child, it listens for every child of the object
//                set data to cardview:
//                String pepTitle = dataSnapshot.getValue(String.class)//not string, do the PepTalkObject itself
//                //or should I be getting whole PepTalkObject and .getTitle from that?
//                cardview.setText(pepTitle)
//
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }
}
