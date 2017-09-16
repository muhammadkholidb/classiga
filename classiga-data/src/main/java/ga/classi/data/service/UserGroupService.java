package ga.classi.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.helper.StringCheck;
import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.error.DataException;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.error.ExceptionCode;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.helper.Dto;
import ga.classi.data.helper.DtoUtils;
import ga.classi.data.repository.MenuPermissionRepository;
import ga.classi.data.repository.UserGroupRepository;
import ga.classi.data.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class UserGroupService extends AbstractServiceHelper {

    private static final Logger log = LoggerFactory.getLogger(UserGroupService.class);

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Transactional(readOnly = true)
    public Dto getAllUserGroups(Dto dtoInput) {

        String searchTerm = dtoInput.getStringValue("searchTerm");

        PageRequest pageRequest = createPageRequest(dtoInput, Sort.Direction.ASC, "name");
        
        Page<UserGroupEntity> pages;

        if (searchTerm == null || searchTerm.isEmpty()) {
            pages = userGroupRepository.findAll(pageRequest);
        } else {
            pages = userGroupRepository.findAllFiltered(searchTerm.toLowerCase(), pageRequest);
        }
        
        return buildResultByPage(pages);
    }

    @Transactional(readOnly = true)
    public Dto getOneUserGroup(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        String strId = dtoInput.getStringValue("id");

        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");

        UserGroupEntity userGroup = userGroupRepository.findOne(Long.valueOf(strId));
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        return buildResultByEntity(userGroup);
    }

    @Transactional(readOnly = true)
    public Dto getOneUserGroupWithMenuPermissions(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        String strId = dtoInput.getStringValue("id");

        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");

        UserGroupEntity userGroup = userGroupRepository.findByIdFetchMenuPermissions(Long.valueOf(strId));
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
    public Dto addUserGroup(Dto dtoInput) {

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
        UserGroupEntity findUserGroup = userGroupRepository.findOneByLowerName(strGroupName.toLowerCase());
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
    public Dto editUserGroup(Dto dtoInput) {
        log.info("Edit user group ...");

        // Validate dtoInput
        log.debug("Validate input ...");
        DataValidation.containsRequiredData(dtoInput, "userGroup", "menuPermissions");

        String strUserGroup = dtoInput.getStringValue("userGroup");
        String strMenuPermissions = dtoInput.getStringValue("menuPermissions");

        // Validate values
        log.debug("Validate values ...");
        DataValidation.validateJSONObject(strUserGroup, "User Group");
        DataValidation.validateJSONArray(strMenuPermissions, "Menu Permissions");

        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);

        // Validate parameter user group
        log.debug("Validate user group parameters ...");
        DataValidation.containsRequiredData(jsonUserGroup, "id", "name", "active");

        String strId = String.valueOf(jsonUserGroup.get("id"));
        String strGroupName = String.valueOf(jsonUserGroup.get("name"));
        String strGroupDescription = String.valueOf(jsonUserGroup.get("description"));
        String strGroupActive = String.valueOf(jsonUserGroup.get("active"));

        // Validate values user group
        log.debug("Validate user group parameter values");
        DataValidation.validateNumeric(strId, "User Group ID");
        DataValidation.validateEmpty(strId, "Name");
        DataValidation.validateYesNo(strGroupActive, "Active");

        List<MenuPermissionEntity> listMenuPermission = new ArrayList<MenuPermissionEntity>();

        // Validate parameter menu permissions
        log.debug("Validate parameter menu permissions ...");
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

        Long userGroupId = Long.valueOf(strId);

        // Find by ID
        log.debug("Find user group by ID ...");
        UserGroupEntity findUserGroupById = userGroupRepository.findOne(userGroupId);
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        // Find another user group having the same name
        log.debug("Find another user group having the same name: {}", strGroupName);
        UserGroupEntity findUserGroupByName = userGroupRepository.findOneByLowerName(strGroupName.toLowerCase());
        if ((findUserGroupByName != null) && (!Objects.equals(findUserGroupByName.getId(), userGroupId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[]{strGroupName});
        }

        findUserGroupById.setName(strGroupName);
        findUserGroupById.setDescription(strGroupDescription);
        findUserGroupById.setActive(strGroupActive.toLowerCase());
        findUserGroupById.setLowerName(strGroupName.toLowerCase());

        // Update user group
        log.debug("Save edited user group ...");
        UserGroupEntity updated = userGroupRepository.save(findUserGroupById);

        // Remove all menus for this user group
        log.debug("Delete menu permissions by user group ID ...");
        menuPermissionRepository.deleteByUserGroup(updated);
        menuPermissionRepository.flush();

        // Save menus
        log.debug("Save menu permissions ...");        
        for (MenuPermissionEntity menuPermission : listMenuPermission) {
            menuPermission.setUserGroup(updated);
            menuPermissionRepository.save(menuPermission);
        }

        return buildResultByEntity(updated);
    }

    @Transactional
    public void removeUserGroup(Dto dtoInput) {

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

        for (Long userGroupId : listUserGroupId) {

            // Count users in this group
            Long countUsers = userRepository.countByUserGroup(new UserGroupEntity(userGroupId));
            
            log.debug("User group {}, count users: {}", userGroupId, countUsers);
            
            if (countUsers > 0) {
                String userGroupName = userGroupRepository.findNameById(userGroupId);
                throw new DataException(ExceptionCode.E1002, ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, new Object[]{userGroupName, countUsers});
            }

            UserGroupEntity userGroup = new UserGroupEntity();
            userGroup.setId(userGroupId);

            // Remove menus for this user group first to avoid foreign key constraint violation
            List<MenuPermissionEntity> deletedPermissions = menuPermissionRepository.deleteByUserGroup(userGroup);
            log.debug("Deleted: {} menu permissions", deletedPermissions.size());
        }

        // Remove user group
        List<UserGroupEntity> deleted = userGroupRepository.deleteByIdIn(listUserGroupId);
        log.debug("Deleted: {} user groups", deleted.size());
    }

}
