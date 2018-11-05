/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.web.utility;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
public class RequestInformation {

    // Read https://gist.github.com/c0rp-aubakirov/a4349cbd187b33138969

    private final HttpServletRequest request;

    public RequestInformation(HttpServletRequest request) {
        this.request = request;
    }

    public void printRequestInfo() {
        final String referer = getReferer();
        final String fullURL = getFullURL();
        final String clientIpAddr = getClientIpAddr();
        final String clientOS = getClientOS();
        final String clientBrowser = getClientBrowser();
        final String userAgent = getUserAgent();
        
        log.info(String.format("%n%-20s %s%n%-20s %s%n%-20s %s%n%-20s %s%n%-20s %s%n%-20s %s%n",
                            "User Agent:", userAgent,
                            "Operating System:", clientOS,
                            "Browser Name:", clientBrowser,
                            "IP Address:", clientIpAddr,
                            "Full URL:", fullURL,
                            "Referrer:", referer));
    }

    public String getReferer() {
        final String referer = request.getHeader("referer");
        return referer;
    }

    public String getFullURL() {
        final StringBuffer requestURL = request.getRequestURL();
        final String queryString = request.getQueryString();

        final String result = queryString == null ? requestURL.toString() : requestURL.append('?')
                .append(queryString)
                .toString();

        return result;
    }

    //http://stackoverflow.com/a/18030465/1845894
    public String getClientIpAddr() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //http://stackoverflow.com/a/18030465/1845894
    public String getClientOS() {
        final String browserDetails = request.getHeader("User-Agent");

        //=================OS=======================
        final String lowerCaseBrowser = browserDetails.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            return "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            return "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            return "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            return "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            return "IPhone";
        } else {
            return "Unknown, More-Info: " + browserDetails;
        }
    }

    //http://stackoverflow.com/a/18030465/1845894
    public String getClientBrowser() {
        final String browserDetails = request.getHeader("User-Agent");
        final String user = browserDetails.toLowerCase();

        String browser = "";

        //===============Browser===========================
        if (user.contains("msie")) {
            String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
                    "/")[0] + "-" + (browserDetails.substring(
                            browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if (user.contains("opr") || user.contains("opera")) {
            if (user.contains("opera")) {
                browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
                        "/")[0] + "-" + (browserDetails.substring(
                                browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if (user.contains("opr")) {
                browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
                        "-")).replace(
                                "OPR", "Opera");
            }
        } else if (user.contains("chrome")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf(
                "mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf(
                "mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox")) {
            browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("rv")) {
            browser = "IE";
        } else {
            browser = "Unknown, More-Info: " + browserDetails;
        }

        return browser;
    }

    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }
}
