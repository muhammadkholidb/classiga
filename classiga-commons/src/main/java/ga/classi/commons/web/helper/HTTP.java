package ga.classi.commons.web.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ga.classi.commons.helper.StringConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The client for processing HTTP based requests.
 * 
 * @author eatonmunoz
 */
@Slf4j
@Setter
@Getter
public class HTTP {

    private String              host;
    private String              path;
    private Map<String, Object> parameters;
    private Map<String, Object> headers;

    public HTTP() {}

    public HTTP(String host) {
        this(host, null, null);
    }

    public HTTP(String host, String path) {
        this(host, path, null);
    }

    public HTTP(String host, String path, Map<String, Object> parameters) {
        this(host, path, parameters, null);
    }

    public HTTP(String host, String path, Map<String, Object> parameters, Map<String, Object> headers) {
        this.host = host;
        this.path = path;
        this.parameters = parameters;
        this.headers = headers;
    }

    /**
     * Builds a URL query string parameters.
     * 
     * @param parameters
     *            The request parameters.
     * @return An URL query string parameters.
     */
    private String buildQueryStrings(Map<String, Object> parameters) throws UnsupportedEncodingException {
        if ((parameters != null) && !parameters.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (Entry<String, Object> entry : parameters.entrySet()) {                
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
        return null;
    }

    /**
     * Sends an HTTP GET request.
     * 
     * @return The response from the HTTP GET request.
     * @throws IOException
     *             If an error occurred during the request.
     */
    public HTTPResponse get() throws IOException {

        if ((host == null) || host.isEmpty()) {
            throw new NullPointerException("Empty host.");
        }

        String strUrl = host + ((path == null) ? "" : path);
        String queryStrings = buildQueryStrings(parameters);

        log.debug("Sending GET request to URL: {}", strUrl);
        log.debug("Parameters: {}", parameters);

        URL url = new URL(strUrl + ((queryStrings == null) ? "" : ("?" + queryStrings)));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if (headers != null) {
            log.debug("Headers: {}", headers);
            for (Entry<String, Object> entry : headers.entrySet()) {                
                con.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }

        int responseCode = con.getResponseCode();
        log.debug("Response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {

            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            log.debug("Response: {}", response);
            return new HTTPResponse(response.toString());
        }

        throw new IOException("Unexpected response code: " + responseCode + " (" + con.getResponseMessage() + ")");
    }

    /**
     * Sends an HTTP POST request.
     * 
     * @return The response from the HTTP POST request.
     * @throws IOException
     *             If an error occurred during the request.
     */
    public HTTPResponse post() throws IOException {

        if ((host == null) || host.isEmpty()) {
            throw new NullPointerException("Empty host.");
        }

        String strUrl = host + ((path == null) ? "" : path);
        String queryStrings = buildQueryStrings(parameters);

        log.debug("Sending POST request to URL: {}", strUrl);
        log.debug("Parameters: {}", parameters);

        URL obj = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        if (headers != null) {
            log.debug("Headers: {}", headers);
            for (Entry<String, Object> entry : headers.entrySet()) {                
                con.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }

        if (queryStrings != null) {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(queryStrings);
            wr.flush();
            wr.close();
        }

        int responseCode = con.getResponseCode();
        log.debug("Response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {

            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            log.debug("Response: {}", response);
            return new HTTPResponse(response.toString());
        }

        throw new IOException("Unexpected response code: " + responseCode + " (" + con.getResponseMessage() + ")");
    }

    /**
     * Adds a parameter to the HTTP request.
     * 
     * @param name
     *            The name of the parameter.
     * @param value
     *            The value of the parameter.
     */
    public void addParameter(String name, Object value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(name, value);
    }

    /**
     * Returns the value of the specified parameter name.
     * 
     * @param name
     *            The name of the parameter.
     * @return The value of the specified parameter name.
     */
    public Object getParameter(String name) {
        if (parameters != null) {
            return parameters.get(name);
        }
        return null;
    }

    /**
     * Sets the request header or adds a new header data.
     * 
     * @param name
     *            The name of the header.
     * @param value
     *            The value of the header.
     */
    public void setHeader(String name, Object value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(name, value);
    }

    /**
     * Returns the value of the specified header name.
     * 
     * @param name
     *            The name of the header.
     * @return The value of the header.
     */
    public Object getHeader(String name) {
        if (headers != null) {
            return headers.get(name);
        }
        return null;
    }

}
