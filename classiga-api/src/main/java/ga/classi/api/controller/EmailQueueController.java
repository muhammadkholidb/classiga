/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.data.helper.DTOUtils;
import ga.classi.data.service.EmailQueueService;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author muhammad
 */
@RestController
@RequestMapping("/email-queue")
public class EmailQueueController extends BaseController {
    
    @Autowired
    private EmailQueueService emailQueueService;
    
    @PostMapping(value = "/add")
    public ResponseObject addUser() throws UnsupportedEncodingException {
        DTO added = emailQueueService.add(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.emailqueue.add"), added);
    }

}
