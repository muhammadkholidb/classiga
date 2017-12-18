package ga.classi.commons.data.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 *
 * @author eatonmunoz
 */
public class DtoUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 
     * @param request
     * @return 
     * @throws java.io.UnsupportedEncodingException 
     */
    public static Dto fromServletRequest(ServletRequest request) throws UnsupportedEncodingException {
        if (request == null) {
            return null;
        }
        Dto dto = new Dto();
        Map<String, String[]> parameters = request.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            if (values.length == 0) {
                dto.put(key, ""); // Put empty string
            } else if (values.length == 1) {
                dto.put(key, URLDecoder.decode(String.valueOf(values[0]), "UTF-8")); // Put the first value
            } else {
                dto.put(key, URLDecoder.decode(Arrays.toString(values), "UTF-8")); // Put all values
            }
        }
        return dto;
    }

    /**
     * 
     * @param list
     * @param excludeKeys
     * @return 
     */
    public static List<Dto> toDtoList(List<?> list, String... excludeKeys) {
        if (list == null) {
            return null;
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
     * 
     * @param object
     * @param excludeKeys
     * @return 
     */
    public static Dto toDto(Object object, String... excludeKeys) {
        if (object == null) {
            return null;
        }
        return omit(MAPPER.convertValue(object, Dto.class), excludeKeys);
    }

    /**
     * 
     * @param <T>
     * @param dto
     * @param t
     * @return 
     */
    public static <T> T toObject(Dto dto, Class<T> t) {
        if (dto == null) {
            return null;
        }
        return MAPPER.convertValue(dto, t);
    }

    /**
     * 
     * @param dto
     * @param keys
     * @return 
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