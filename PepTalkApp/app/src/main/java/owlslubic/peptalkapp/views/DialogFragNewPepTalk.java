package owlslubic.peptalkapp.views;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 8/28/16.
 */
public class DialogFragNewPepTalk extends DialogFragment implements TextView.OnEditorActionListener{
    private EditText mTitleInput, mBodyInput;
    private TextView mDialogTitle;
    public Button mButton;


    //empty constructor
    public DialogFragNewPepTalk(){}

    public static DialogFragNewPepTalk newInstance(){
        DialogFragNewPepTalk frag = new DialogFragNewPepTalk();
        return frag;
    }

    //define listener interface which will pass back data result
    public interface NewPepTalkListener{
        void onFinishEditDialog(String inputText, View view);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_peptalk, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDialogTitle = (TextView) view.findViewById(R.id.textview_new_peptalk);
        mTitleInput = (EditText) view.findViewById(R.id.edittext_new_peptalk_title);
        mBodyInput = (EditText) view.findViewById(R.id.edittext_new_peptalk);

        //show soft keyboard automatically
        mTitleInput.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //set up callback when the "done" button is pressed
        mTitleInput.setOnEditorActionListener(this);
    }

    public Bundle getInputs(){
        String title = mTitleInput.getText().toString();
        String body = mBodyInput.getText().toString();
        Bundle args = new Bundle();
        args.putString("title", title);
//        args.putString("body", body);

        return args;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
       if(EditorInfo.IME_ACTION_DONE == i){
           //return input text back to activity via listener
           NewPepTalkListener listener = (NewPepTalkListener) getActivity();
           listener.onFinishEditDialog(mTitleInput.getText().toString(),getView());
           dismiss();
           return true;
       }
        return false;
    }
}
