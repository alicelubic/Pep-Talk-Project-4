package owlslubic.peptalkapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 9/20/16.
 */

public class AddPepTalkWidgetActivity extends AppCompatActivity {
EditText mTitle, mBody;
    Button mSubmit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_widget);

        mTitle = (EditText) findViewById(R.id.edittext_widget_new_peptalk_title);
        mBody = (EditText) findViewById(R.id.edittext_widget_new_peptalk_body);
        mSubmit = (Button) findViewById(R.id.button_widget_submit_activity);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleInput = mTitle.getText().toString().trim();
                String bodyInput = mBody.getText().toString().trim();

                if (titleInput.equalsIgnoreCase("") || titleInput.equalsIgnoreCase(" ")) {
                    mTitle.setError("oops! please enter a valid title");
                } else if (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" ")) {
                    mBody.setError("oops! please enter valid text");
                } else {
                    CustomDialog.writeNewPeptalk(titleInput, bodyInput);
                    Toast.makeText(AddPepTalkWidgetActivity.this, "pep talk added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
