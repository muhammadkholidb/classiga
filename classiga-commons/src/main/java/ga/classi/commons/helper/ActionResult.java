package ga.classi.commons.helper;

import java.util.Map;

import org.json.simple.JSONValue;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ActionResult {

    @Setter
    protected String status;

    @Setter
    protected String message;

    @SuppressWarnings("rawtypes")
    @Setter
    protected Map data;
    
    protected Object content;
    protected Integer countRows;
    protected Integer totalRows;
    protected Integer totalPages;

    public ActionResult() {}

    @SuppressWarnings("rawtypes")
    public ActionResult(String status, String message, Map data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    public ActionResult parseData() {

        if (this.data != null) {

            Object oCountRows = this.data.get(CommonConstants.COUNT_ROWS);
            Object oTotalRows = this.data.get(CommonConstants.TOTAL_ROWS);
            Object oTotalPages = this.data.get(CommonConstants.TOTAL_PAGES);

            if (oCountRows != null) {
                this.countRows = Integer.valueOf(oCountRows + "");
            }

            if (oTotalRows != null) {
                this.totalRows = Integer.valueOf(oTotalRows + "");
            }

            if (oTotalPages != null) {
                this.totalPages = Integer.valueOf(oTotalPages + "");
            }

            String toJSONString = JSONValue.toJSONString(this.data.get(CommonConstants.CONTENT));
            this.content = JSONValue.parse(toJSONString);
        }
        
        return this;
    }
    
}
