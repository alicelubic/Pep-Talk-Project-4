package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.PepTalkAdapter;

public class PepTalkListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pep_talk_list);

        //dummy data
        ArrayList<PepTalkObject> peptalks = new ArrayList<>();
        peptalks.add(new PepTalkObject("For when you're feelin' like a failure","You're not!",false));
        peptalks.add(new PepTalkObject("Go have a quick cry in the bathroom","You're not!",false));
        peptalks.add(new PepTalkObject("It's not the end of the world!","You're not!",false));
        peptalks.add(new PepTalkObject("you're okAY!","You're not!",false));

        //recyclerview for the cardviews that each display a pep talk title
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        PepTalkAdapter adapter = new PepTalkAdapter(this, peptalks);
        recyclerView.setAdapter(adapter);

        //fab, onClick launches dialog
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_checklist);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //launch new pep talk dialog
//                Toast.makeText(PepTalkListActivity.this, "new pep talk comin' your way!", Toast.LENGTH_SHORT).show();
//            }
//        });

        //inflate menu that has back home button, and maybe some sort of info overflow menu.. do i need it?


    }
}
