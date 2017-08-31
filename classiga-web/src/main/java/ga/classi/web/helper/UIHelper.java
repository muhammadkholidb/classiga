package ga.classi.web.helper;

import ga.classi.web.ui.Notify;

/**
 *
 * @author eatonmunoz
 */
public class UIHelper {

    public static Notify createNotification(String title, String message, String notificationType) {
        return new Notify(title, message, notificationType);
    }
    
    public static Notify createErrorNotification(String title, String message) {
        return new Notify(title, message, Notify.DANGER);
    }
    
    public static Notify createErrorNotification(String message) {
        return new Notify(message, Notify.DANGER);
    }
    
    public static Notify createInfoNotification(String title, String message) {
        return new Notify(title, message, Notify.INFO);
    }
    
    public static Notify createInfoNotification(String message) {
        return new Notify(message, Notify.INFO);
    }
    
    public static Notify createSuccessNotification(String title, String message) {
        return new Notify(title, message, Notify.SUCCESS);
    }
    
    public static Notify createSuccessNotification(String message) {
        return new Notify(message, Notify.SUCCESS);
    }
    
    public static Notify createWarningNotification(String title, String message) {
        return new Notify(title, message, Notify.WARNING);
    }
    
    public static Notify createWarningNotification(String message) {
        return new Notify(message, Notify.WARNING);
    }
    
}
