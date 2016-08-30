package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class ChecklistItemObject {
    String mText;
    boolean mChecked;

    public ChecklistItemObject(String mText, boolean mIsChecked) {
        this.mText = mText;
        this. mChecked = mIsChecked;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public boolean ismChecked() {
        return  mChecked;
    }

    public void setmChecked(boolean mIsChecked) {
        this. mChecked = mIsChecked;
    }
}
