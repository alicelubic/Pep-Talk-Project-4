package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class ChecklistItemObject {
    String text;
    String key;
    String notes;
    boolean isChecked;

    //empty constructor so that firebase realtime database can use this
    public ChecklistItemObject() {
    }

    public ChecklistItemObject(String key, String text, String notes, boolean isChecked) {
        this.key = key;
        this.text = text;
        this.notes = notes;
        this.isChecked = isChecked;
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


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}

