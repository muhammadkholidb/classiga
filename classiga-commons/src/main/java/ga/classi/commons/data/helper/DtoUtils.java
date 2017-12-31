package ga.classi.commons.data.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * A utility class for Dto processing.
 * @author eatonmunoz
 */
public class DtoUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DtoUtils() {
        // Restrict instantiation
    }
    
    /**
     * Converts request parameters to Dto object.
     * @param request Servlet request to get the parameters from.
     * @return Dto object from the request parameters.
     * @throws java.io.UnsupportedEncodingException When the parameter value cannot be URL decoded.
     */
    public static Dto fromServletRequest(ServletRequest request) throws UnsupportedEncodingException {
        if (request == null) {
            return null;
        }
        Dto dto = new Dto();
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            switch (values.length) {
                case 0:
                    dto.put(key, ""); // Put empty string
                    break;
                case 1:
                    dto.put(key, URLDecoder.decode(String.valueOf(values[0]), "UTF-8")); // Put the first value
                    break;
                default:
                    dto.put(key, URLDecoder.decode(Arrays.toString(values), "UTF-8")); // Put all values
                    break;
            }
        }
        return dto;
    }

    /**
     * Converts list of object to list of Dto. 
     * @param list List of object to be converted.
     * @param excludeKeys Keys to be excluded from the Dto. These keys will not be available in the result Dto.
     * @return A new list of Dto.
     */
    public static List<Dto> toDtoList(List<?> list, String... excludeKeys) {
        if (list == null) {
            return Collections.emptyList();
        }
        List<Dto> listDto = new ArrayList<Dto>();
        for (Object o : list) {
            if (o == null) {
                continue;
            }
            listDto.add(omit(MAPPER.convertValue(o, Dto.class), excludeKeys));
        }
        return listDto;
    }

    /**
     * Converts any kind of object to Dto. The conversion is done with the help of ObjectMapper from the Jackson library.
     * @param object Object to convert.
     * @param excludeKeys Keys to be excluded from the Dto. These keys will not be available in the result Dto.
     * @return A Dto from the given object.
     */
    public static Dto toDto(Object object, String... excludeKeys) {
        if (object == null) {
            return null;
        }
        return omit(MAPPER.convertValue(object, Dto.class), excludeKeys);
    }

    /**
     * Converts a Dto to the specified object. The conversion is done with the help of ObjectMapper from the Jackson library.
     * @param <T> Any type of object.
     * @param dto A Dto to convert.
     * @param t An object type as the type of the final result.
     * @return A new object with the specified type.
     */
    public static <T> T toObject(Dto dto, Class<T> t) {
        if (dto == null) {
            return null;
        }
        return MAPPER.convertValue(dto, t);
    }

    /**
     * Omits the specified keys from a Dto.
     * @param dto A Dto to process.
     * @param keys The keys to be removed from the given Dto.
     * @return The same Dto with the keys removed.
     */
    public static Dto omit(Dto dto, String... keys) {
        if (dto == null) {
            return null;
        }
        if (keys != null) {
            for (String key : keys) {
                dto.remove(key);
            }
        }
        return dto;
    }

}
