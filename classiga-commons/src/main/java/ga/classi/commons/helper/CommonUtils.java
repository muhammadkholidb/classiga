package ga.classi.commons.helper;

import java.util.HashMap;
import java.util.Map;

public final class CommonUtils {

    private CommonUtils() {
        // Restrict instantiation
    }

    public static String getExceptionMessage(Exception exception) {
        String message = exception.getMessage();
        return ((message == null) || "".equals(message)) ? exception.toString() : message;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> map(Object... keysAndValues) {
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments must be even.");
        }
        Map<K, V> map = new HashMap<K, V>();
        for (int i = 0; i < keysAndValues.length;) {
            map.put((K) keysAndValues[i++], (V) keysAndValues[i++]);
        }
        return map;
    }

}
