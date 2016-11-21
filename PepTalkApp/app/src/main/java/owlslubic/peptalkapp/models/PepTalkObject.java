package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkObject {
    private String key;
    private String title;
    private String body;
    private String lastEdited;
    private boolean isWidgetDefault;

    //empty constructor so that firebase realtime database can use this
    public PepTalkObject() {
    }

    public PepTalkObject(String key, String title, String body, boolean isWidgetDefault) {
        this.key = key;
        this.title = title;
        this.body = body;
        this.isWidgetDefault = isWidgetDefault;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isWidgetDefault() {
        return this.isWidgetDefault;
    }

    public void setWidgetDefault(boolean isWidgetDefault) {
        this.isWidgetDefault = isWidgetDefault;
    }

}
