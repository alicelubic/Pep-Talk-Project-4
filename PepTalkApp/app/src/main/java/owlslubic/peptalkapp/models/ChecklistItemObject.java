package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class ChecklistItemObject {
    String text;
//    boolean isChecked;

    public ChecklistItemObject() {}

    public ChecklistItemObject(String text){//}, boolean isChecked) {
        this.text = text;
//        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setIsChecked(boolean isChecked) {
//        this.isChecked = isChecked;
//    }
}

