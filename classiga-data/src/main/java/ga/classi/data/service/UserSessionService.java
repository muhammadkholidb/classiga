/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;


import static ga.classi.commons.constant.RequestDataConstants.TOKEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.data.entity.UserSessionEntity;
import ga.classi.data.helper.DataValidator;
import ga.classi.data.repository.UserSessionRepository;

@Service
public class UserSessionService extends AbstractServiceHelper {

    @Autowired
    private UserSessionRepository userSessionRepository;
    
    @Transactional
    public DTO updateUserSession(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(TOKEN);

        // Validate values
        String token = validator.validateEmptyString(TOKEN);

        UserSessionEntity userSession = userSessionRepository.findOneByTokenAndDeleted(token, CommonConstants.NO);
        if (userSession == null) {
            throw new DataException(Errors.USER_SESSION_NOT_FOUND);
        }

        // Update datetime
        userSession.setUpdateTimeMillis(System.currentTimeMillis());
        userSession = userSessionRepository.saveAndFlush(userSession);
        
        return buildResultByEntity(userSession);
    }

}
