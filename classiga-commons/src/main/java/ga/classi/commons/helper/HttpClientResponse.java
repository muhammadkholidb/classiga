package ga.classi.commons.helper;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class HttpClientResponse {

    private String response;
    private String status;
    private String message;
    private JSONObject data;
    private Object content;
    private Integer countRows;
    private Integer totalRows;
    private Integer totalPages;

    public HttpClientResponse(String response) {
        this.response = response;
        try {
            JSONObject json = (JSONObject) JSONValue.parseWithException(response);
            this.status = (String) json.get(CommonConstants.STATUS);
            this.message = (String) json.get(CommonConstants.MESSAGE);
            this.data = (JSONObject) json.get(CommonConstants.DATA);
            
            if (this.data != null) {                
                
                Object countRows = this.data.get(CommonConstants.COUNT_ROWS);
                Object totalRows = this.data.get(CommonConstants.TOTAL_ROWS);
                Object totalPages = this.data.get(CommonConstants.TOTAL_PAGES);
                
                if (countRows != null) {                
                    this.countRows = Integer.valueOf(countRows+ "");
                }
                
                if (totalRows != null) {
                    this.totalRows = Integer.valueOf(totalRows + "");
                }
                
                if (totalPages != null) {
                    this.totalPages = Integer.valueOf(totalPages + "");                
                }

                this.content = this.data.get(CommonConstants.CONTENT);
            }
            
        } catch (Exception e) {
            log.error("Error parsing HttpClient response.", e); 
        }
    }

    @Override
    public String toString() {
        return getResponse();
    }

}
