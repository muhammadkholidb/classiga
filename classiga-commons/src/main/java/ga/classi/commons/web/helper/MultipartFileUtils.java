package ga.classi.commons.web.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultipartFileUtils {

    private MultipartFileUtils() {}
    
    // List of MIME types https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
    
    public static final MediaType TEXT_CSV         = MediaType.parseMediaType("text/csv");
    public static final MediaType APPLICATION_DOC  = MediaType.parseMediaType("application/msword");
    public static final MediaType APPLICATION_DOCX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MediaType APPLICATION_XLS  = MediaType.parseMediaType("application/vnd.ms-excel");
    public static final MediaType APPLICATION_XLSX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MediaType APPLICATION_PPT  = MediaType.parseMediaType("application/vnd.ms-powerpoint");
    public static final MediaType APPLICATION_PPTX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MediaType APPLICATION_JAR  = MediaType.parseMediaType("application/java-archive");
    public static final MediaType APPLICATION_ZIP  = MediaType.parseMediaType("application/zip");
    
    public static MediaType getMediaType(MultipartFile file) {
        try {
            return MediaType.parseMediaType(file.getContentType());
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String getFileExtension(MultipartFile file) {
        String ext = "";
        String[] commonExts = getCommonExtensions(file);
        String filename = file.getOriginalFilename();
        int lastDotIdx = filename.lastIndexOf('.');
        if (lastDotIdx != -1) {            
            ext = filename.substring(lastDotIdx, filename.length());
        }
        if (ext.isEmpty()) {
            ext = (commonExts.length > 0) ? commonExts[0] : "";
        } else if ((commonExts.length > 0) && (Arrays.asList(commonExts).contains(ext))) {
            ext = commonExts[0];
        }
        return ext;
    }
    
    public static String[] getCommonExtensions(MultipartFile file) {
        MediaType mediaType = getMediaType(file);
        if (mediaType == null) {
            return new String[] {};
        }
        if (MediaType.IMAGE_GIF.equals(mediaType)) {
            return new String[] {".gif"};
        }
        if (MediaType.IMAGE_JPEG.equals(mediaType)) {
            return new String[] {".jpg", ".jpeg"};
        }
        if (MediaType.IMAGE_PNG.equals(mediaType)) {
            return new String[] {".png"};
        }
        if (MediaType.APPLICATION_JSON.equals(mediaType)) {
            return new String[] {".json"};
        }
        if (MediaType.APPLICATION_PDF.equals(mediaType)) {
            return new String[] {".pdf"};
        }
        if (MediaType.APPLICATION_XHTML_XML.equals(mediaType)) {
            return new String[] {".xhtml"};
        }
        if (MediaType.APPLICATION_XML.equals(mediaType)) {
            return new String[] {".xml"};
        }
        if (MediaType.TEXT_HTML.equals(mediaType)) {
            return new String[] {".html", ".htm"};
        }
        if (MediaType.TEXT_MARKDOWN.equals(mediaType)) {
            return new String[] {".md"};
        }
        if (MediaType.TEXT_XML.equals(mediaType)) {
            return new String[] {".xml"};
        }
        if (MediaType.TEXT_PLAIN.equals(mediaType)) {
            return new String[] {".txt"};
        }
        // More media types
        if (TEXT_CSV.equals(mediaType)) {
            return new String[] {".csv"};
        }
        if (APPLICATION_DOC.equals(mediaType)) {
            return new String[] {".doc"};
        }
        if (APPLICATION_DOCX.equals(mediaType)) {
            return new String[] {".docx"};
        }
        if (APPLICATION_PPT.equals(mediaType)) {
            return new String[] {".ppt"};
        }
        if (APPLICATION_PPTX.equals(mediaType)) {
            return new String[] {".pptx"};
        }
        if (APPLICATION_XLS.equals(mediaType)) {
            return new String[] {".xls"};
        }
        if (APPLICATION_XLSX.equals(mediaType)) {
            return new String[] {".xlsx"};
        }
        if (APPLICATION_JAR.equals(mediaType)) {
            return new String[] {".jar"};
        }
        if (APPLICATION_ZIP.equals(mediaType)) {
            return new String[] {".zip"};
        }
        return new String[] {};
    }

    public static int upload(MultipartFile file, String directory, String filename) {
        if ((file == null) || file.isEmpty()) {
            return 0;
        }
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File toFile = new File(dir.getAbsolutePath() + File.separator + filename);
            return IOUtils.copy(file.getInputStream(), new FileOutputStream(toFile));
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            return -1;
        }
    }
    
}
