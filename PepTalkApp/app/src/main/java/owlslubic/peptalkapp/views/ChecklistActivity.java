package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.ChecklistViewHolder;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;

public class ChecklistActivity extends AppCompatActivity {

    private static final String TAG = "ChecklistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_checklist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<ChecklistItemObject, ChecklistViewHolder> adapter =
                new FirebaseRecyclerAdapter<ChecklistItemObject, ChecklistViewHolder>
                        (ChecklistItemObject.class, R.layout.card_checklist, ChecklistViewHolder.class, dbRef.child("Checklist")) {
                    @Override
                    protected void populateViewHolder(ChecklistViewHolder holder, final ChecklistItemObject model, int position) {
                        Log.i(TAG, "populateViewHolder: " + model.getText());
                        holder.mItem.setText(model.getText());
                        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                CustomDialog.launchEditChecklistDialog(ChecklistActivity.this, model);
                                return true;
                            }
                        });
                    }
                };

        recyclerView.setAdapter(adapter);


        //fab for add new checklist item
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_checklist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.launchNewChecklistDialog(ChecklistActivity.this);
                Toast.makeText(ChecklistActivity.this, "get ready to check another one off the list!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
