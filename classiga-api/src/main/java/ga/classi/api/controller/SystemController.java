package ga.classi.api.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.data.helper.DTOUtils;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.data.helper.DataImporter;
import ga.classi.data.service.SystemService;
import ga.classi.api.helper.ResponseObject;
import java.io.UnsupportedEncodingException;
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
@RequestMapping("/settings")
public class SystemController extends BaseController {
	
    @Autowired
    private SystemService systemService;

    @Autowired
    protected DataImporter dataImporter;
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/system/list", method = RequestMethod.GET)
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
        
        return new ResponseObject(CommonConstants.SUCCESS, dto);
    }

    @RequestMapping(value = "/system/edit", method = RequestMethod.POST)
    public ResponseObject editSystemList() throws UnsupportedEncodingException {
        DTO dto = systemService.editSystemList(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyEditSystem"), dto);
    }

}
