/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.web.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import ga.classi.commons.constant.StringConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * The client for processing HTTP based requests.
 * 
 * @author muhammad
 */
@Slf4j
@Setter
@Getter
public class HTTP {

    private static final RestTemplate REST = new RestTemplate();

    private String url;
    private HttpHeaders headers;
    private Map<String, ?> body;
    
    public static HTTP create(String url, Map<String, ?> body, HttpHeaders headers) {
        return new HTTP();
    }
    
    public HTTPResponse request(HttpMethod method) throws IOException {
        HttpEntity<Map<String, ?>> requestEntity;
        ResponseEntity<String> response;
        if (HttpMethod.GET == method) {
            
            // If http GET, than the parameters should be set as URI variables
            requestEntity = new HttpEntity(headers);
            response = REST.exchange(url + "?" + buildQueryString(body), method, requestEntity, String.class, new Object[0]);
            
        } else {
            
            // Else than http GET, set the parameters in request body
            // Spring RestTemplate requires body parameters must be in type of MultiValueMap
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            if (body != null) {
                body.entrySet().forEach((entry) -> {
                    try {
                        map.add(entry.getKey(), URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                        log.warn("Unable to encode request value, send without encoding");
                        map.add(entry.getKey(), entry.getValue());
                    }
                });
            }
            requestEntity = new HttpEntity(map, headers);
            response = REST.exchange(url, method, requestEntity, String.class, new Object[0]);
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            return new HTTPResponse(response.getBody());
        }
        throw new IOException("Response code: " + response.getStatusCodeValue() + " (" + response.getBody() + ")");
    }
    
    public HTTPResponse get() throws IOException {
        return request(HttpMethod.GET);
    }
    
    public HTTPResponse post() throws IOException {
        return request(HttpMethod.POST);
    }
    
    public HTTPResponse put() throws IOException {
        return request(HttpMethod.PUT);
    }
    
    public HTTPResponse patch() throws IOException {
        return request(HttpMethod.PATCH);
    }
    
    public HTTPResponse delete() throws IOException {
        return request(HttpMethod.DELETE);
    }
    
    /**
     * Builds a query string parameters to append to a URL.
     * 
     * @param parameters The request parameters.
     * @return An URL query string parameters.
     * @throws java.io.UnsupportedEncodingException
     */
    public static String buildQueryString(Map<String, ?> parameters) throws UnsupportedEncodingException {
        if ((parameters != null) && !parameters.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (Entry<String, ?> entry : parameters.entrySet()) {                
                builder.append(entry.getKey());
                builder.append("=");
                builder.append((entry.getValue() == null) ? StringConstants.EMPTY: URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                i++;
                if (i < parameters.size()) {
                    builder.append("&");
                }
            }
            return builder.toString();
        }
        return "";
    }

}
