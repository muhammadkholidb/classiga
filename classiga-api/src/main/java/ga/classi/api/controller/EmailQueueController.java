/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.api.controller;

import ga.classi.api.helper.ResponseObject;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.data.service.EmailQueueService;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
@RestController
@RequestMapping("/email-queues")
public class EmailQueueController extends BaseController {
    
    @Autowired
    private EmailQueueService emailQueueService;
    
    @GetMapping
    public ResponseObject getEmailQueuesByStatus(String status) throws UnsupportedEncodingException {
        log.info("Get email queues");
        DTO result = emailQueueService.getAllByStatus(new DTO().put("status", status));
        return new ResponseObject(CommonConstants.SUCCESS, result);
    }

    @PostMapping
    public ResponseObject addEmailQueue() throws UnsupportedEncodingException {
        log.info("Add email queue");
        DTO added = emailQueueService.add(DTOUtils.fromServletRequest(request));
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.emailqueue.add"), added);
    }

    @PutMapping("/{srh}")
    public ResponseObject editEmailQueue(@PathVariable String srh) throws UnsupportedEncodingException {
        log.info("Edit email queue");
        DTO edited = emailQueueService.edit(DTOUtils.fromServletRequest(request).put("srh", srh));
        log.info("Edited: {}", edited);
        return new ResponseObject(CommonConstants.SUCCESS, getResponseMessage("success.emailqueue.edit"), edited);
    }

}
