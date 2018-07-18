package ga.classi.web.filter;

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
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompressResponseFilter implements Filter {

    // Read http://www.byteslounge.com/tutorials/how-to-compress-response-html-in-java-web-application
    
    private static final String PATH_RESOURCES  = "/resources";
    private static final String PATH_AVATAR     = "/avatar";
    private static final String PATH_LIST       = "/list";

    private HtmlCompressor compressor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.debug("Do filter ...");

        String mode = System.getProperty("mode");

        if (mode != null && "dev".equalsIgnoreCase(mode)) { 
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contextPath = httpRequest.getContextPath();
        String requestUrl = httpRequest.getRequestURI();
        String resourceUrl = contextPath + PATH_RESOURCES;
        String avatarUrl = contextPath + PATH_AVATAR;

        if (requestUrl.startsWith(resourceUrl) || requestUrl.startsWith(avatarUrl) || requestUrl.endsWith(PATH_LIST)) {
            chain.doFilter(request, response);
            return;
        }

        HtmlResponseWrapper responseWrapper = new HtmlResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, responseWrapper);

        String servletResponse = responseWrapper.getCaptureAsString();
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
        // Filter destroy
    }

    // Read https://www.leveluplunch.com/java/tutorials/034-modify-html-response-using-filter/
    
    private class HtmlResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream capture;
        private ServletOutputStream output;
        private PrintWriter writer;

        public HtmlResponseWrapper(HttpServletResponse response) {
            super(response);
            capture = new ByteArrayOutputStream(response.getBufferSize());
        }

        @Override
        public ServletOutputStream getOutputStream() {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called on this response.");
            }

            if (output == null) {
                output = new ServletOutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        capture.write(b);
                    }

                    @Override
                    public void flush() throws IOException {
                        capture.flush();
                    }

                    @Override
                    public void close() throws IOException {
                        capture.close();
                    }

                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public void setWriteListener(WriteListener arg0) {
                        // Set writer listener
                    }
                };
            }

            return output;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (output != null) {
                throw new IllegalStateException("getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                writer = new PrintWriter(new OutputStreamWriter(capture, getCharacterEncoding()));
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            super.flushBuffer();

            if (writer != null) {
                writer.flush();
            } else if (output != null) {
                output.flush();
            }
        }

        public byte[] getCaptureAsBytes() throws IOException {
            if (writer != null) {
                writer.close();
            } else if (output != null) {
                output.close();
            }

            return capture.toByteArray();
        }

        public String getCaptureAsString() throws IOException {
            return new String(getCaptureAsBytes(), getCharacterEncoding());
        }

    }

}
