package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 8/30/16.
 */
public class CustomDialog extends AlertDialog {

    //dont think i need a constructor but whatever
    protected CustomDialog(Context context) {
        super(context);
    }

    //if all this goes well, I'll write some sort of switch statement for layouts so that there's only one method to do all dis launchin

    public static void launchNewPeptalkDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_new_peptalk, null);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView header = (TextView) dialog.findViewById(R.id.textview_new_peptalk);
        EditText title = (EditText) dialog.findViewById(R.id.edittext_new_peptalk_title);
        EditText body = (EditText) dialog.findViewById(R.id.edittext_new_peptalk);

        //send the info off to firebase
        // objectRef.push().setValue(title.getText().toString())...
        //etc body.getText().toString

    }

    public void launchEditPeptalkDialog(Context context, int position) {
        //gotta find out how to populate an edittext with existing content to edit it...
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_edit_peptalk, null);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void launchNewChecklistDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_new_checklist, null);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void launchEditChecklistDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_edit_checklist, null);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    //this will be what is launched if all the checklist items are checked
    public void launchChecklistCompleteDialog() {
    }
    //in case i cant get the fragment display to work, or if I need to launch it from the peptalk list activity i just want a backup
    public void launchPeptalkDisplayDialog() {
    }


    //not sure WHERE this will need to be, whether dialog or activity
    //to write to firebase:


}
