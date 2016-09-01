package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class ChecklistItemObject {
    String text;
    String id;
//    boolean isChecked;

    public ChecklistItemObject() {}


    public ChecklistItemObject(String id, String text){//}, boolean isChecked) {
        this.id = id;
        this.text = text;
//        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }



//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setIsChecked(boolean isChecked) {
//        this.isChecked = isChecked;
//    }
}

