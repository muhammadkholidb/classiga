/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.ui;

/**
 *
 * @author muhammad
 */
public class Notify {

    public static final String INFO = "info";
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";
    public static final String DANGER = "danger";
    
    private String title;
    private String message;
    private String type;

    public Notify() {
        
    }
    
    public Notify(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }
    
    public Notify(String message, String type) {
        this("", message, type);
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
