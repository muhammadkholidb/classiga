/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.DTO;
import ga.classi.commons.utility.StringCheck;
import ga.classi.data.entity.EmailQueueEntity;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.DataValidation;
import ga.classi.commons.data.constant.QueueStatus;
import ga.classi.commons.web.utility.JSON;
import ga.classi.data.repository.EmailQueueRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author muhammad
 */
@Slf4j
@Service
public class EmailQueueService extends AbstractServiceHelper {
    
    @Autowired
    private EmailQueueRepository emailQueueRepository;
    
    @Transactional(readOnly = true)
    public DTO getAllByStatus(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "status");

        // Validate values
        String strStatus = dtoInput.getStringValue("status");
        DataValidation.validateNumber(strStatus, "Status");

        Integer queueStatus = Integer.valueOf(strStatus);
        
        if (!QueueStatus.isValidId(queueStatus)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_QUEUE_STATUS);            
        }
        
        List<EmailQueueEntity> emailQueues = emailQueueRepository.findByStatusAndDeleted(queueStatus, CommonConstants.NO);
        
        return buildResultByEntityList(emailQueues);
    }

    @Transactional
    public DTO add(DTO dtoInput) {
        DataValidation.containsRequiredData(dtoInput, "to", "subject", "status");
        
        String strTo = dtoInput.getStringValue("to");
        String strSubject = dtoInput.getStringValue("subject");
        String strStatus = dtoInput.getStringValue("status");
        String strTemplate = dtoInput.getStringValue("template");
        String strMessage = dtoInput.getStringValue("message");
        String strData = dtoInput.getStringValue("data");
        
        DataValidation.validateEmail(strTo);
        DataValidation.validateEmpty(strSubject, "Subject");
        DataValidation.validateNumber(strStatus, "Status");
        
        if (StringCheck.isEmpty(strTemplate) && StringCheck.isEmpty(strMessage)) {
            throw new DataException(ExceptionCode.E1009, ErrorMessageConstants.EMPTY_TEMPLATE_AND_MESSAGE);
        }
        
        Integer status = Integer.valueOf(strStatus);
        
        if (!QueueStatus.isValidId(status)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_QUEUE_STATUS);
        }
        
        EmailQueueEntity emailQueue = new EmailQueueEntity();
        emailQueue.setTo(strTo);
        emailQueue.setSubject(strSubject);
        emailQueue.setStatus(status);
        emailQueue.setTemplate(strTemplate);
        emailQueue.setMessage(strMessage);
        emailQueue.setData(strData);
        
        emailQueue = emailQueueRepository.save(emailQueue);
        
        return buildResultByEntity(emailQueue);
    }
    
    @Transactional
    public DTO edit(DTO dtoInput) {
        DataValidation.containsRequiredData(dtoInput, "srh", "to", "subject", "status");
        
        String strSrh = dtoInput.getStringValue("srh");
        String strTo = dtoInput.getStringValue("to");
        String strSubject = dtoInput.getStringValue("subject");
        String strStatus = dtoInput.getStringValue("status");
        String strTemplate = dtoInput.getStringValue("template");
        String strMessage = dtoInput.getStringValue("message");
        String strData = dtoInput.getStringValue("data");
        
        DataValidation.validateEmail(strTo);
        DataValidation.validateEmpty(strSubject, "Subject");
        DataValidation.validateNumber(strStatus, "Status");
        
        if (StringCheck.isEmpty(strTemplate) && StringCheck.isEmpty(strMessage)) {
            throw new DataException(ExceptionCode.E1009, ErrorMessageConstants.EMPTY_TEMPLATE_AND_MESSAGE);
        }
        
        Integer status = Integer.valueOf(strStatus);
        
        if (!QueueStatus.isValidId(status)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_QUEUE_STATUS);
        }
        
        EmailQueueEntity emailQueue = emailQueueRepository.findOneBySrh(strSrh);
        if (emailQueue == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.EMAIL_QUEUE_NOT_FOUND);
        }
        
        emailQueue.setTo(strTo);
        emailQueue.setSubject(strSubject);
        emailQueue.setStatus(status);
        emailQueue.setTemplate(strTemplate);
        emailQueue.setMessage(strMessage);
        emailQueue.setData(strData);
        
        emailQueue = emailQueueRepository.saveAndFlush(emailQueue);
        
        return buildResultByEntity(emailQueue);
    }
    
}
