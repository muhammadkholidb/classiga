/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.utility;

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
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i = i + 2) {
            map.put((K) keysAndValues[i], (V) keysAndValues[i + 1]);
        }
        return map;
    }

    public static String concat(Object... objs) {
        if ((objs == null) || (objs.length == 0)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : objs) {
            sb.append(o);
        }
        return sb.toString();
    }

}
