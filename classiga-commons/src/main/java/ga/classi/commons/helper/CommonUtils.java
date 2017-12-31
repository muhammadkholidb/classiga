package ga.classi.commons.helper;

public final class CommonUtils {

    public static String getExceptionMessage(Exception exception) {
        String message = exception.getMessage();
        return ((message == null) || "".equals(message)) ? exception.toString() : message;
    }

    private CommonUtils() {
        // Restrict instantiation
    }
    
}
