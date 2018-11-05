/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import static ga.classi.api.constant.RequestMappingConstants.SYSTEM_EDIT;
import static ga.classi.api.constant.RequestMappingConstants.SYSTEM_LIST;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.data.helper.DataImporter;
import ga.classi.data.service.SystemService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * To Read:
 * https://www.genuitec.com/spring-frameworkrestcontroller-vs-controller/
 *
 */
@Slf4j
@RestController
public class SystemController extends BaseController {
	
    @Autowired
    private SystemService systemService;

    @Autowired
    protected DataImporter dataImporter;
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = SYSTEM_LIST, method = RequestMethod.GET)
    public ResponseObject getAllSystem() {
        
        DTO dto = systemService.getAllSystem(new DTO());

        // Import default system data when no data returned
        if ((dto == null) || dto.isEmpty() || dto.get("content") == null || ((List) dto.get("content")).isEmpty() ) {

            log.debug("System data is empty, load initial data ...");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("dataset/system.xml");
            dataImporter.addStreamDataSet(is);
            dataImporter.importAll();
            
            // Find again
            dto = systemService.getAllSystem(null);
        }
        
        return ResponseObject.success(dto);
    }

    @RequestMapping(value = SYSTEM_EDIT, method = RequestMethod.POST)
    public ResponseObject editSystemList() throws UnsupportedEncodingException {
        DTO dto = systemService.editSystemList(DTOUtils.fromServletRequest(request));
        return ResponseObject.success(getResponseMessage("success.SuccessfullyEditSystem"), dto);
    }

}
