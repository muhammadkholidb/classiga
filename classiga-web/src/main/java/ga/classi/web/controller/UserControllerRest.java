package ga.classi.web.controller;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.web.controller.base.BaseControllerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
@RestController
public class UserControllerRest extends BaseControllerAdapter {

    private static final String[] SORT_COLUMN_NAME_BY_NUMBER = new String[] {
            "fullName", 
            "fullName", 
            "username", 
            "email", 
            "active", 
            "userGroup.lowerName", 
            "fullName"}; 
    
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/settings/user/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject getUsers() {

        log.info("Get users ...");
        
        JSONObject params = new JSONObject();
        params.put("start", httpServletRequest.getParameter("start").trim());
        params.put("length", httpServletRequest.getParameter("length").trim());
        params.put("searchTerm", httpServletRequest.getParameter("searchTerm"));
        params.put("sortOrder", httpServletRequest.getParameter("sortOrder").trim());
        params.put("sortColumn", getSortColumnName(httpServletRequest.getParameter("sortColumnIndex").trim()));
        
        ActionResult result = listUser(params);

        JSONObject json = new JSONObject();
        
        if (CommonConstants.SUCCESS.equals(result.getStatus())) {
            json.put("recordsFiltered", result.getTotalRows());
            json.put("data", (JSONArray) result.getContent());
        } else {
            json.put("recordsFiltered", 0);
            json.put("data", new JSONArray());
            json.put("error", result.getMessage());
        }
        
        return json;
    }

    private String getSortColumnName(String columnIndex) {
        Integer index = Integer.valueOf(columnIndex);
        return SORT_COLUMN_NAME_BY_NUMBER[index];
    }
    
}
