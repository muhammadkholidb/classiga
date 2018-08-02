/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
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
public class UserGroupControllerRest extends BaseControllerAdapter {

    private static final String[] SORT_COLUMN_NAME_BY_NUMBER = new String[] {
            "name", 
            "name", 
            "description", 
            "active", 
            "name"}; 
    
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/settings/user-group/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject getUserGroups() {

        log.info("Get user groups ...");
        
        JSONObject params = new JSONObject();
        params.put("start", httpServletRequest.getParameter("start").trim());
        params.put("length", httpServletRequest.getParameter("length").trim());
        params.put("searchTerm", httpServletRequest.getParameter("searchTerm"));
        params.put("sortOrder", httpServletRequest.getParameter("sortOrder").trim());
        params.put("sortColumn", getSortColumnName(httpServletRequest.getParameter("sortColumnIndex").trim()));
        
        ActionResult response = listUserGroup(params);

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
