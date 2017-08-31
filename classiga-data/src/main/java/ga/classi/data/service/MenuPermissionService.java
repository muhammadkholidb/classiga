package ga.classi.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.helper.Dto;
import ga.classi.data.repository.MenuPermissionRepository;

@Service
public class MenuPermissionService extends AbstractServiceHelper {

    @Autowired
    private MenuPermissionRepository userGroupMenuPermissionRepository;

    @Transactional(readOnly = true)
    public Dto getAllMenuPermissions(Dto dtoInput) {

        // No validation
        List<MenuPermissionEntity> list = userGroupMenuPermissionRepository.findAll();
        
        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public Dto getMenuPermissionListByUserGroupId(Dto dtoInput) {

        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "userGroupId");

        String strUserGroupId = dtoInput.getStringValue("userGroupId");

        // Validate values
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");

        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(Long.valueOf(strUserGroupId));

        List<MenuPermissionEntity> list = userGroupMenuPermissionRepository.findByUserGroup(userGroup);
        
        return buildResultByEntityList(list);
    }

}
