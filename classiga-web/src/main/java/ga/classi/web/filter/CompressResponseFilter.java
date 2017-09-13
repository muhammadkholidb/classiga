package ga.classi.web.filter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompressResponseFilter implements Filter {

    // Read http://www.byteslounge.com/tutorials/how-to-compress-response-html-in-java-web-application

    private static final String RESOURCE_PATH = "/res/";
    private static final String AJAX_PATH = "/ajax/";
    
    private HtmlCompressor compressor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.debug("Do filter ...");
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contextPath = httpRequest.getContextPath();
        String requestUrl = httpRequest.getRequestURI();
        String resourceUrl = contextPath + RESOURCE_PATH;
        String ajaxUrl = contextPath + AJAX_PATH;

        if (requestUrl.startsWith(resourceUrl) || requestUrl.startsWith(ajaxUrl)) {
            chain.doFilter(request, response);
            return;
        }
        
        CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, responseWrapper);

        String servletResponse = responseWrapper.toString();
        response.getWriter().write(compressor.compress(servletResponse));
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        compressor = new HtmlCompressor();
        compressor.setCompressCss(true);
        compressor.setCompressJavaScript(true);
    }

    @Override
    public void destroy() {

    }

    private class CharResponseWrapper extends HttpServletResponseWrapper {

        private final CharArrayWriter output;

        @Override
        public String toString() {
            return output.toString();
        }

        public CharResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new CharArrayWriter();
        }

        @Override
        public PrintWriter getWriter() {
            return new PrintWriter(output);
        }

    }

}
