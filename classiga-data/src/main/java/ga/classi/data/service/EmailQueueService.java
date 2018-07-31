/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.entity.EmailQueueEntity;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.helper.EmailQueueStatus;
import ga.classi.data.repository.EmailQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author muhammad
 */
@Service
public class EmailQueueService extends AbstractServiceHelper {
    
    @Autowired
    private EmailQueueRepository emailQueueRepository;
    
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
        
        if (!EmailQueueStatus.isValidId(status)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_EMAIL_QUEUE_STATUS);
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
    
}
