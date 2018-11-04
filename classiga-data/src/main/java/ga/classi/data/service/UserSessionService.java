/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;


import ga.classi.commons.constant.CommonConstants;
import static ga.classi.commons.constant.RequestDataConstants.*;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.data.entity.UserSessionEntity;
import ga.classi.data.helper.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ga.classi.data.repository.UserSessionRepository;

@Service
public class UserSessionService extends AbstractServiceHelper {

    @Autowired
    private UserSessionRepository userSessionRepository;
    
    public DTO getUserSessionByToken(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(TOKEN);

        // Validate values
        String token = validator.validateEmptyString(TOKEN);

        UserSessionEntity userSession = userSessionRepository.findOneByTokenAndDeleted(token, CommonConstants.NO);
        if (userSession == null) {
            throw new DataException(Errors.USER_SESSION_NOT_FOUND);
        }

        return buildResultByEntity(userSession);
    }

}
