/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.DTO;
import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.repository.MenuPermissionRepository;
import ga.classi.data.repository.UserGroupRepository;

@Service
public class MenuPermissionService extends AbstractServiceHelper {

    @Autowired
    private UserGroupRepository userGroupRepository;
    
    @Autowired
    private MenuPermissionRepository userGroupMenuPermissionRepository;

    @Transactional(readOnly = true)
    public DTO getAllMenuPermissions(DTO dtoInput) {

        // No validation
        List<MenuPermissionEntity> list = userGroupMenuPermissionRepository.findAll();
        
        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public DTO getMenuPermissionListByUserGroupId(DTO dtoInput) {

        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "userGroupId");

        String strUserGroupId = dtoInput.getStringValue("userGroupId");

        // Validate values
        DataValidation.validateNumber(strUserGroupId, "User Group ID");

        UserGroupEntity userGroup = userGroupRepository.findOne(Long.valueOf(strUserGroupId));
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        List<MenuPermissionEntity> list = userGroupMenuPermissionRepository.findByUserGroup(userGroup);
        
        return buildResultByEntityList(list);
    }

}
