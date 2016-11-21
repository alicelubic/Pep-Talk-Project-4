package owlslubic.peptalkapp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.presenters.FirebaseHelper;

/**
 * Created by owlslubic on 9/20/16.
 */

public class AddPepTalkWidgetActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mTitle, mBody;
    ImageButton mSubmit, mCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_widget);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = (EditText) findViewById(R.id.edittext_widget_new_peptalk_title);
        mBody = (EditText) findViewById(R.id.edittext_widget_new_peptalk_body);
        mSubmit = (ImageButton) findViewById(R.id.button_widget_submit_activity);
        mCancel = (ImageButton) findViewById(R.id.imagebutton_widget_cancel);


        mSubmit.setOnClickListener(this);
        mCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_widget_submit_activity:
                String titleInput = mTitle.getText().toString().trim();
                String bodyInput = mBody.getText().toString().trim();

                if (titleInput.equalsIgnoreCase("") || titleInput.equalsIgnoreCase(" ")) {
                    mTitle.setError("oops! please enter a valid title");
                } else if (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" ")) {
                    mBody.setError("oops! please enter valid text");
                } else {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseHelper.writeNewPepTalk(titleInput, bodyInput, AddPepTalkWidgetActivity.this, false);
                        Toast.makeText(AddPepTalkWidgetActivity.this, "pep talk added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddPepTalkWidgetActivity.this, "Please sign in to add a pep talk", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.imagebutton_widget_cancel:
                finish();
                break;
        }
    }
}
