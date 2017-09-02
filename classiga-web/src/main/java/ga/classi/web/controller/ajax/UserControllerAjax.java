package ga.classi.web.controller.ajax;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.HttpClient;
import ga.classi.commons.helper.HttpClientResponse;
import ga.classi.web.controller.HttpClientBaseController;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
@RestController
public class UserControllerAjax extends HttpClientBaseController {

    private static final String[] SORT_COLUMN_NAME_BY_NUMBER = new String[] {
            "fullName", 
            "fullName", 
            "username", 
            "email", 
            "active", 
            "userGroup.lowerName", 
            "fullName"}; 
    
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/ajax/settings/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject getUser() throws IOException {

        log.debug("Get user data ...");
        
        HttpClient httpClient = getDefinedHttpClient("/settings/user/list");
        
        httpClient.addParameter("start", httpServletRequest.getParameter("start").trim());
        httpClient.addParameter("length", httpServletRequest.getParameter("length").trim());
        httpClient.addParameter("searchTerm", httpServletRequest.getParameter("searchTerm"));
        httpClient.addParameter("sortOrder", httpServletRequest.getParameter("sortOrder").trim());
        httpClient.addParameter("sortColumn", getSortColumnName(httpServletRequest.getParameter("sortColumnIndex").trim()));
        
        HttpClientResponse response = httpClient.get();

        JSONObject json = new JSONObject();
        
        if (CommonConstants.SUCCESS.equals(response.getStatus())) {
            json.put("recordsFiltered", response.getTotalRows());
            json.put("data", (JSONArray) response.getContent());
        } else {
            json.put("recordsFiltered", 0);
            json.put("data", new JSONArray());
            json.put("error", response.getMessage());
        }
        
        return json;
    }

    private String getSortColumnName(String columnIndex) {
        Integer index = Integer.valueOf(columnIndex);
        return SORT_COLUMN_NAME_BY_NUMBER[index];
    }
    
}
