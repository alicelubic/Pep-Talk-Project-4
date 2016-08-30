package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkObject {
    String mTitle;
    String mBody;
    boolean mIsWidgetDefault;

    public PepTalkObject(String mTitle, String mBody, boolean mIsWidgetDefault) {
        this.mTitle = mTitle;
        this.mBody = mBody;
        this.mIsWidgetDefault = mIsWidgetDefault;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public boolean ismIsWidgetDefault() {
        return mIsWidgetDefault;
    }

    public void setmIsWidgetDefault(boolean mIsWidgetDefault) {
        this.mIsWidgetDefault = mIsWidgetDefault;
    }
}
