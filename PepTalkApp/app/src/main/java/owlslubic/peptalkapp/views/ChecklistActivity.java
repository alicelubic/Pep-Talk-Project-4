package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.presenters.ChecklistAdapter;

public class ChecklistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        //dummy data
        ArrayList<ChecklistItemObject> checklistList = new ArrayList<>();
        checklistList.add(new ChecklistItemObject("Drink some water", false));
        checklistList.add(new ChecklistItemObject("Eat some food", false));
        checklistList.add(new ChecklistItemObject("Give yourself a hug", false));
        checklistList.add(new ChecklistItemObject("Take a nap", false));
        checklistList.add(new ChecklistItemObject("Take a moment, deep breath", false));



        //recyclerview for cardviews with checkboxes
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_checklist);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        ChecklistAdapter adapter = new ChecklistAdapter(this, checklistList);
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
