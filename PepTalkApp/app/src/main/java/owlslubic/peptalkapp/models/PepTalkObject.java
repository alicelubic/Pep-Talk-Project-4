package owlslubic.peptalkapp.models;

/**
 * Created by owlslubic on 8/30/16.
 */
public class PepTalkObject {
    String id;
    String title;
    String body;
//    boolean isWidgetDefault;

    public PepTalkObject() {}

    public PepTalkObject(String id, String title, String body){//, boolean isWidgetDefault) {
        this.id = id;
        this.title = title;
        this.body = body;
//        this.isWidgetDefault = isWidgetDefault;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


//    public boolean isWidgetDefault() {
//        return this.isWidgetDefault;
//    }

//    public void setIsWidgetDefault(boolean isWidgetDefault) {
//        this.isWidgetDefault = isWidgetDefault;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }



}
