/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import static ga.classi.commons.constant.RequestDataConstants.USER_GROUP_ID;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.helper.DataValidator;
import ga.classi.data.repository.MenuPermissionRepository;
import ga.classi.data.repository.UserGroupRepository;

@Service
public class MenuPermissionService extends AbstractServiceHelper {

    @Autowired
    private UserGroupRepository userGroupRepository;
    
    @Autowired
    private MenuPermissionRepository userGroupMenuPermissionRepository;

    public DTO getAllMenuPermissions(DTO dtoInput) {

        // No validation
        List<MenuPermissionEntity> list = userGroupMenuPermissionRepository.findAll();
        
        return buildResultByEntityList(list);
    }

    public DTO getMenuPermissionListByUserGroupId(DTO dtoInput) {

        // Validate parameters
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(USER_GROUP_ID);

        // Validate values
        String strUserGroupId = validator.validateNumber(USER_GROUP_ID);

        UserGroupEntity userGroup = userGroupRepository.findOne(Long.valueOf(strUserGroupId));
        if (userGroup == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }

        List<MenuPermissionEntity> list = userGroupMenuPermissionRepository.findByUserGroup(userGroup);
        
        return buildResultByEntityList(list);
    }

}
