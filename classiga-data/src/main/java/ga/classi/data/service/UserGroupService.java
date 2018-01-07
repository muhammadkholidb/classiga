package ga.classi.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.data.helper.DtoUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.repository.MenuPermissionRepository;
import ga.classi.data.repository.UserGroupRepository;
import ga.classi.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserGroupService extends AbstractServiceHelper {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Transactional(readOnly = true)
    public Dto getAll(Dto dtoInput) {

        String searchTerm = dtoInput.getStringValue("searchTerm");

        PageRequest pageRequest = createPageRequest(dtoInput, Sort.Direction.ASC, "name");
        
        Page<UserGroupEntity> pages;

        if (searchTerm == null || searchTerm.isEmpty()) {
            pages = userGroupRepository.findByDeleted(CommonConstants.NO, pageRequest);
        } else {
            pages = userGroupRepository.findByDeletedSearch(CommonConstants.NO, searchTerm.toLowerCase(), pageRequest);
        }
        
        return buildResultByPage(pages);
    }

    @Transactional(readOnly = true)
    public Dto getOne(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        String strId = dtoInput.getStringValue("id");

        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");

        UserGroupEntity userGroup = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        return buildResultByEntity(userGroup);
    }

    @Transactional(readOnly = true)
    public Dto getOneWithMenuPermissions(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        String strId = dtoInput.getStringValue("id");

        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");

        UserGroupEntity userGroup = userGroupRepository.findByIdAndDeletedFetchMenuPermissions(Long.valueOf(strId), CommonConstants.NO);
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        List<MenuPermissionEntity> menuPermissions = userGroup.getMenuPermissions();

        Dto dtoUserGroup = DtoUtils.toDto(userGroup);
        List<Dto> listDtoMenuPermissions = DtoUtils.toDtoList(menuPermissions, "userGroup");

        return buildResultByDto(dtoUserGroup.put("menuPermissions", listDtoMenuPermissions));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Dto add(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroup", "menuPermissions");

        String strUserGroup = dtoInput.getStringValue("userGroup");
        String strMenuPermissions = dtoInput.getStringValue("menuPermissions");

        // Validate values
        DataValidation.validateJSONObject(strUserGroup, "User Group");
        DataValidation.validateJSONArray(strMenuPermissions, "Menu Permissions");

        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);

        // Validate parameter user group
        DataValidation.containsRequiredData(jsonUserGroup, "name", "active");

        String strGroupName = String.valueOf(jsonUserGroup.get("name"));
        String strGroupActive = String.valueOf(jsonUserGroup.get("active"));
        String strGroupDescription = String.valueOf(jsonUserGroup.get("description"));

        // Validate values user group
        DataValidation.validateEmpty(strGroupName, "Name");
        DataValidation.validateYesNo(strGroupActive, "Active");

        List<MenuPermissionEntity> listMenuPermission = new ArrayList<MenuPermissionEntity>();

        // Validate parameter menu permissions
        for (Object object : arrMenuPermissions) {

            JSONObject jsonMenuPermission = (JSONObject) object;

            // Validate dtoInput
            DataValidation.containsRequiredData(jsonMenuPermission, "menuCode", "canView", "canModify");

            String strMenuCode = String.valueOf(jsonMenuPermission.get("menuCode"));
            String strViewPermission = String.valueOf(jsonMenuPermission.get("canView"));
            String strModifyPermission = String.valueOf(jsonMenuPermission.get("canModify"));

            // Validate values
            DataValidation.validateEmpty(strMenuCode, "Menu Code");
            DataValidation.validateYesNo(strViewPermission, "View Permission");
            DataValidation.validateYesNo(strModifyPermission, "Modify Permission");

            MenuPermissionEntity menuPermission = new MenuPermissionEntity();
            menuPermission.setMenuCode(strMenuCode);
            menuPermission.setCanView(strViewPermission.toLowerCase());
            menuPermission.setCanModify(strModifyPermission.toLowerCase());

            listMenuPermission.add(menuPermission);
        }

        // Find other user group with name
        UserGroupEntity findUserGroup = userGroupRepository.findOneByLowerNameAndDeleted(strGroupName.toLowerCase(), CommonConstants.NO);
        if (findUserGroup != null) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[]{strGroupName});
        }

        // Save new user group
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setName(strGroupName);
        userGroup.setDescription(strGroupDescription);
        userGroup.setActive(strGroupActive.toLowerCase());
        userGroup.setLowerName(strGroupName.toLowerCase());
        UserGroupEntity inserted = userGroupRepository.save(userGroup);

        // Save menu permissions
        for (MenuPermissionEntity menuPermission : listMenuPermission) {
            menuPermission.setUserGroup(inserted);
            menuPermissionRepository.save(menuPermission);
        }

        return buildResultByEntity(inserted);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Dto edit(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroup", "menuPermissions");

        String strUserGroup = dtoInput.getStringValue("userGroup");
        String strMenuPermissions = dtoInput.getStringValue("menuPermissions");

        // Validate values
        DataValidation.validateJSONObject(strUserGroup, "User Group");
        DataValidation.validateJSONArray(strMenuPermissions, "Menu Permissions");

        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);

        // Validate parameter user group
        DataValidation.containsRequiredData(jsonUserGroup, "id", "name", "active");

        String strId = String.valueOf(jsonUserGroup.get("id"));
        String strGroupName = String.valueOf(jsonUserGroup.get("name"));
        String strGroupDescription = String.valueOf(jsonUserGroup.get("description"));
        String strGroupActive = String.valueOf(jsonUserGroup.get("active"));

        // Validate values user group
        DataValidation.validateNumeric(strId, "User Group ID");
        DataValidation.validateEmpty(strId, "Name");
        DataValidation.validateYesNo(strGroupActive, "Active");

        Long userGroupId = Long.valueOf(strId);

        // Find by ID
        UserGroupEntity findUserGroupById = userGroupRepository.findOneByIdAndDeleted(userGroupId, CommonConstants.NO);
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        // Find another user group having the same name
        UserGroupEntity findUserGroupByName = userGroupRepository.findOneByLowerNameAndDeleted(strGroupName.toLowerCase(), CommonConstants.NO);
        if ((findUserGroupByName != null) && (!Objects.equals(findUserGroupByName.getId(), userGroupId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[]{strGroupName});
        }

        List<MenuPermissionEntity> listMenuPermission = new ArrayList<MenuPermissionEntity>();

        // Validate parameter menu permissions
        for (Object object : arrMenuPermissions) {

            JSONObject jsonMenuPermission = (JSONObject) object;

            // Validate dtoInput
            DataValidation.containsRequiredData(jsonMenuPermission, "menuCode", "canView", "canModify");

            String strMenuCode = String.valueOf(jsonMenuPermission.get("menuCode"));
            String strViewPermission = String.valueOf(jsonMenuPermission.get("canView"));
            String strModifyPermission = String.valueOf(jsonMenuPermission.get("canModify"));

            // Validate values
            DataValidation.validateEmpty(strMenuCode, "Menu Code");
            DataValidation.validateYesNo(strViewPermission, "View Permission");
            DataValidation.validateYesNo(strModifyPermission, "Modify Permission");

            MenuPermissionEntity menuPermission = new MenuPermissionEntity();
            menuPermission.setMenuCode(strMenuCode);
            menuPermission.setCanView(strViewPermission.toLowerCase());
            menuPermission.setCanModify(strModifyPermission.toLowerCase());

            listMenuPermission.add(menuPermission);
        }

        findUserGroupById.setName(strGroupName);
        findUserGroupById.setDescription(strGroupDescription);
        findUserGroupById.setActive(strGroupActive.toLowerCase());
        findUserGroupById.setLowerName(strGroupName.toLowerCase());

        // Update user group
        UserGroupEntity updated = userGroupRepository.save(findUserGroupById);

        // Remove all menus for this user group
        menuPermissionRepository.deleteByUserGroup(updated);
        menuPermissionRepository.flush();

        // Save menus        
        for (MenuPermissionEntity menuPermission : listMenuPermission) {
            menuPermission.setUserGroup(updated);
            menuPermissionRepository.save(menuPermission);
        }

        return buildResultByEntity(updated);
    }

    @Transactional
    public void remove(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        String strUserGroupId = dtoInput.getStringValue("id");

        List<Long> listUserGroupId = new ArrayList<Long>();

        if (StringCheck.isJSONArray(strUserGroupId)) {

            JSONArray arr = (JSONArray) JSONValue.parse(strUserGroupId);

            for (Object id : arr) {
                String strId = String.valueOf(id);
                DataValidation.validateNumeric(strId, "User Group ID");
                listUserGroupId.add(Long.valueOf(strId));
            }

        } else {

            DataValidation.validateNumeric(strUserGroupId, "User Group ID");
            listUserGroupId.add(Long.valueOf(strUserGroupId));
        }

        List<UserGroupEntity> listUserGroup = new ArrayList<UserGroupEntity>();
        
        for (Long userGroupId : listUserGroupId) {

            UserGroupEntity findUserGroupById = userGroupRepository.findOneByIdAndDeleted(userGroupId, CommonConstants.NO);
            if (findUserGroupById == null) {
                throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
            }

            // Count users in this group
            Long countUsers = userRepository.countByUserGroupAndDeleted(findUserGroupById, CommonConstants.NO);
            
            log.debug("User group {}, count users: {}", userGroupId, countUsers);
            
            if (countUsers > 0) {
                throw new DataException(ExceptionCode.E1002, 
                        ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, 
                        new Object[] { findUserGroupById.getName(), countUsers } );
            }

            // Remove menus for this user group
            List<MenuPermissionEntity> deletedPermissions = menuPermissionRepository.deleteByUserGroup(findUserGroupById);
            log.debug("Deleted: {} menu permissions", deletedPermissions.size());
            
            findUserGroupById.setDeleted();
            listUserGroup.add(findUserGroupById);
        }

        // Remove user group
        List<UserGroupEntity> updated = userGroupRepository.save(listUserGroup);
        log.debug("Updated: {} user groups", updated.size());
    }

}
