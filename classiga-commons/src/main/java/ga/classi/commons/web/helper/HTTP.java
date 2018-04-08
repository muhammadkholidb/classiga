package ga.classi.commons.web.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;

import ga.classi.commons.helper.StringConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The client for processing HTTP based requests.
 * @author eatonmunoz
 */
@Slf4j
@Setter
@Getter
public class HTTP {

    // Read
    // https://stackoverflow.com/questions/3842823/should-logger-be-private-static-or-not
    // https://stackoverflow.com/questions/6653520/why-do-we-declare-loggers-static-final
    // https://stackoverflow.com/questions/1417190/should-a-static-final-logger-be-declared-in-upper-case
    
    public static final String GET = "GET";
    public static final String POST = "POST";

    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";

    private String scheme;
    private String method;
    private String host;
    private String path;
    private JSONObject parameters;
    private JSONObject headers;
    private boolean secure;

    public HTTP() {}

    public HTTP(String host) {
        this(host, null, null);
    }

    public HTTP(String host, String path) {
        this(host, path, null);
    }

    public HTTP(String host, String path, JSONObject parameters) {
        this(host, path, parameters, null);
    }

    public HTTP(String host, String path, JSONObject parameters, JSONObject headers) {
        this(host, path, parameters, headers, false);
    }

    public HTTP(String host, String path, JSONObject parameters, JSONObject headers, boolean secure) {
        this.host = host;
        this.path = path;
        this.parameters = parameters;
        this.headers = headers;
        this.secure = secure;
        try {
            this.scheme = new URL(host).getProtocol();
        } catch (MalformedURLException ex) {
            this.scheme = HTTP_SCHEME;
        }
    }

    public HTTPResponse request() throws IOException {
        if (POST.equals(method)) {
            return post();
        } else if (GET.equals(method)) {
            return get();
        }
        throw new UnsupportedOperationException("Unsupported request method: " + method);
    }

    /**
     * Builds a URL query string parameters from a JSONObject.
     * @param parameters The parameters in JSONObject format.
     * @return An URL query string parameters.
     */
    private String buildQueryStrings(JSONObject parameters) throws UnsupportedEncodingException {
        if ((parameters != null) && !parameters.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (Object key : parameters.keySet()) {
                builder.append(key);
                builder.append("=");
                builder.append(parameters.get(key) == null ? StringConstants.EMPTY : URLEncoder.encode(parameters.get(key).toString(), "UTF-8"));
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
     * @return The response from the HTTP GET request.
     * @throws IOException If an error occurred during the request.
     */
    public HTTPResponse get() throws IOException {

        method = GET;

        if ((host == null) || "".equals(host)) {
            throw new NullPointerException("Empty host.");
        }

        String prefixSchemeHttp = HTTP_SCHEME + "://";
        String prefixSchemeHttps = HTTPS_SCHEME + "://";

        if (host.toLowerCase().startsWith(prefixSchemeHttp)) {
            host = host.substring(prefixSchemeHttp.length());
            secure = false;
        } else if (host.toLowerCase().startsWith(prefixSchemeHttps)) {
            host = host.substring(prefixSchemeHttps.length());
            secure = true;
        }

        String strUrl = secure ? (prefixSchemeHttps + host + path) : (prefixSchemeHttp + host + path);
        String queryStrings = buildQueryStrings(parameters);

        // Read http://slf4j.org/faq.html#logging_performance
        log.debug("Sending GET request to URL: {}", strUrl);
        log.debug("Parameters: {}", parameters);

        URL url = new URL(strUrl + ((queryStrings == null) ? "" : ("?" + queryStrings)));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);
        if (headers != null) {
            log.debug("Headers: {}", headers);
            for (Object name : headers.keySet()) {
                con.setRequestProperty((String) name, (String) headers.get(name));
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
     * @return The response from the HTTP POST request.
     * @throws IOException If an error occurred during the request.
     */
    public HTTPResponse post() throws IOException {

        method = POST;

        if ((host == null) || "".equals(host)) {
            throw new NullPointerException("Empty host.");
        }

        String prefixSchemeHttp = HTTP_SCHEME + "://";
        String prefixSchemeHttps = HTTPS_SCHEME + "://";

        if (host.toLowerCase().startsWith(prefixSchemeHttp)) {
            host = host.substring(prefixSchemeHttp.length());
            secure = false;
        } else if (host.toLowerCase().startsWith(prefixSchemeHttps)) {
            host = host.substring(prefixSchemeHttps.length());
            secure = true;
        }

        String strUrl = secure ? (prefixSchemeHttps + host + path) : (prefixSchemeHttp + host + path);
        String queryStrings = buildQueryStrings(parameters);

        log.debug("Sending POST request to URL: {}", strUrl);
        log.debug("Parameters: {}", parameters);

        URL obj = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod(method);
        con.setDoOutput(true);
        if (headers != null) {
            log.debug("Headers: {}", headers);
            for (Object name : headers.keySet()) {
                con.setRequestProperty((String) name, (String) headers.get(name));
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
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     */
    @SuppressWarnings("unchecked")
    public void addParameter(String name, Object value) {
        if (parameters == null) {
            parameters = new JSONObject();
        }
        parameters.put(name, value);
    }

    /**
     * Returns the value of the specified parameter name.
     * @param name The name of the parameter.
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
     * @param name The name of the header.
     * @param value The value of the header.
     */
    @SuppressWarnings("unchecked")
    public void setHeader(String name, Object value) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put(name, value);
    }

    /**
     * Returns the value of the specified header name.
     * @param name The name of the header.
     * @return The value of the header.
     */
    public Object getHeader(String name) {
        if (headers != null) {
            return headers.get(name);
        }
        return null;
    }

}
