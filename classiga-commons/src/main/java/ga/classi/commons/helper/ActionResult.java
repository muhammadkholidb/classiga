package ga.classi.commons.helper;

import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ActionResult {

    @Setter
    protected String status;

    @Setter
    protected String message;

    @Setter
    protected JSONObject data;
    
    protected Object content;
    protected Integer countRows;
    protected Integer totalRows;
    protected Integer totalPages;

    public ActionResult() {}

    public ActionResult parseData() {

        if (this.data != null) {

            Object countRows = this.data.get(CommonConstants.COUNT_ROWS);
            Object totalRows = this.data.get(CommonConstants.TOTAL_ROWS);
            Object totalPages = this.data.get(CommonConstants.TOTAL_PAGES);

            if (countRows != null) {
                this.countRows = Integer.valueOf(countRows + "");
            }

            if (totalRows != null) {
                this.totalRows = Integer.valueOf(totalRows + "");
            }

            if (totalPages != null) {
                this.totalPages = Integer.valueOf(totalPages + "");
            }

            this.content = this.data.get(CommonConstants.CONTENT);
        }
        
        return this;
    }
    
}
