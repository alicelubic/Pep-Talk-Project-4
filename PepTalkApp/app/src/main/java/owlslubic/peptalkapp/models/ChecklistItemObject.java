package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class ChecklistItemObject {
    String text;
    String key;
//    boolean isChecked;

    public ChecklistItemObject() {}


    public ChecklistItemObject(String key, String text){//}, boolean isChecked) {
        this.key = key;
        this.text = text;
//        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }



//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setIsChecked(boolean isChecked) {
//        this.isChecked = isChecked;
//    }
}

