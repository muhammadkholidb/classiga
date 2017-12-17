package ga.classi.rest.controller.settings;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.data.helper.DtoUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.helper.DataImporter;
import ga.classi.data.service.SystemService;
import ga.classi.rest.controller.BaseController;
import ga.classi.rest.helper.ResponseObject;
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
    @RequestMapping(value = "/system/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject getAllSystem() throws Exception {
        
        Dto dto = systemService.getAllSystem(new Dto());

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

    @RequestMapping(value = "/system/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject editSystemList() throws Exception {

        Dto dto = systemService.editSystemList(DtoUtils.fromServletRequest(request));

        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyEditSystem"), dto);
    }

}
