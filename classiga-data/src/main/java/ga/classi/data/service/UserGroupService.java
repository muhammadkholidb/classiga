/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import static ga.classi.commons.constant.RequestDataConstants.ACTIVE;
import static ga.classi.commons.constant.RequestDataConstants.CAN_MODIFY;
import static ga.classi.commons.constant.RequestDataConstants.CAN_VIEW;
import static ga.classi.commons.constant.RequestDataConstants.DESCRIPTION;
import static ga.classi.commons.constant.RequestDataConstants.ID;
import static ga.classi.commons.constant.RequestDataConstants.MENU_CODE;
import static ga.classi.commons.constant.RequestDataConstants.MENU_PERMISSIONS;
import static ga.classi.commons.constant.RequestDataConstants.NAME;
import static ga.classi.commons.constant.RequestDataConstants.SEARCH_TERM;
import static ga.classi.commons.constant.RequestDataConstants.USER_GROUP;

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

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.commons.data.utility.DTOUtils;
import ga.classi.commons.utility.StringCheck;
import ga.classi.data.entity.MenuPermissionEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.helper.DataValidator;
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
    public DTO getAll(DTO dtoInput) {

        String searchTerm = dtoInput.getStringValue(SEARCH_TERM);

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
    public DTO getOne(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID);

        // Validate values
        String strId = validator.validateNumber(ID);

        UserGroupEntity userGroup = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
        if (userGroup == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }

        return buildResultByEntity(userGroup);
    }

    @Transactional(readOnly = true)
    public DTO getOneWithMenuPermissions(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID);

        // Validate values
        String strId = validator.validateNumber(ID);

        UserGroupEntity userGroup = userGroupRepository.findByIdAndDeletedFetchMenuPermissions(Long.valueOf(strId), CommonConstants.NO);
        if (userGroup == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }

        List<MenuPermissionEntity> menuPermissions = userGroup.getMenuPermissions();

        DTO dtoUserGroup = DTOUtils.toDTO(userGroup);
        List<DTO> listDtoMenuPermissions = DTOUtils.toDTOList(menuPermissions, USER_GROUP);

        return buildResultByDTO(dtoUserGroup.put(MENU_PERMISSIONS, listDtoMenuPermissions));
    }

    @Transactional
    public DTO add(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(USER_GROUP, MENU_PERMISSIONS);

        // Validate values
        String strUserGroup = validator.validateJSONObject(USER_GROUP);
        String strMenuPermissions = validator.validateJSONArray(MENU_PERMISSIONS);

        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);

        // Validate parameter user group
        DataValidator validatorUserGroup = new DataValidator(new DTO(jsonUserGroup));
        validatorUserGroup.containsRequiredData(NAME, ACTIVE);

        // Validate values user group
        String strGroupName = validatorUserGroup.validateEmptyString(NAME);
        String strGroupActive = validatorUserGroup.validateYesNo(ACTIVE);

        String strGroupDescription = String.valueOf(jsonUserGroup.get(DESCRIPTION));

        List<MenuPermissionEntity> listMenuPermission = new ArrayList<>();

        // Validate parameter menu permissions
        for (Object object : arrMenuPermissions) {

            JSONObject jsonMenuPermission = (JSONObject) object;

            // Validate dtoInput
            DataValidator validatorMenu = new DataValidator(new DTO(jsonMenuPermission));
            validatorMenu.containsRequiredData(MENU_CODE, CAN_VIEW, CAN_MODIFY);

            // Validate values
            String strMenuCode = validatorMenu.validateEmptyString(MENU_CODE);
            String strViewPermission = validatorMenu.validateYesNo(CAN_VIEW);
            String strModifyPermission = validatorMenu.validateYesNo(CAN_MODIFY);

            MenuPermissionEntity menuPermission = new MenuPermissionEntity();
            menuPermission.setMenuCode(strMenuCode);
            menuPermission.setCanView(strViewPermission.toLowerCase());
            menuPermission.setCanModify(strModifyPermission.toLowerCase());

            listMenuPermission.add(menuPermission);
        }

        // Find other user group with name
        UserGroupEntity findUserGroup = userGroupRepository.findOneByLowerNameAndDeleted(strGroupName.toLowerCase(), CommonConstants.NO);
        if (findUserGroup != null) {
            throw new DataException(Errors.USER_GROUP_ALREADY_EXISTS, new Object[]{strGroupName});
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

    @Transactional
    public DTO edit(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(USER_GROUP, MENU_PERMISSIONS);

        // Validate values
        String strUserGroup = validator.validateJSONObject(USER_GROUP);
        String strMenuPermissions = validator.validateJSONArray(MENU_PERMISSIONS);

        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);

        // Validate parameter user group
        DataValidator validatorUserGroup = new DataValidator(new DTO(jsonUserGroup));
        validatorUserGroup.containsRequiredData(ID, NAME, ACTIVE);

        // Validate values user group
        String strId = validatorUserGroup.validateNumber(ID);
        String strGroupName = validatorUserGroup.validateEmptyString(NAME);
        String strGroupActive = validatorUserGroup.validateYesNo(ACTIVE);

        String strGroupDescription = String.valueOf(jsonUserGroup.get(DESCRIPTION));
        
        Long userGroupId = Long.valueOf(strId);

        // Find by ID
        UserGroupEntity findUserGroupById = userGroupRepository.findOneByIdAndDeleted(userGroupId, CommonConstants.NO);
        if (findUserGroupById == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }

        // Find another user group having the same name
        UserGroupEntity findUserGroupByName = userGroupRepository.findOneByLowerNameAndDeleted(strGroupName.toLowerCase(), CommonConstants.NO);
        if ((findUserGroupByName != null) && (!Objects.equals(findUserGroupByName.getId(), userGroupId))) {
            throw new DataException(Errors.USER_GROUP_ALREADY_EXISTS, new Object[]{strGroupName});
        }

        List<MenuPermissionEntity> listMenuPermission = new ArrayList<>();

        // Validate parameter menu permissions
        for (Object object : arrMenuPermissions) {

            JSONObject jsonMenuPermission = (JSONObject) object;

            // Validate dtoInput
            DataValidator validatorMenu = new DataValidator(new DTO(jsonMenuPermission));
            validatorMenu.containsRequiredData(MENU_CODE, CAN_VIEW, CAN_MODIFY);

            // Validate values
            String strMenuCode = validatorMenu.validateEmptyString(MENU_CODE);
            String strViewPermission = validatorMenu.validateYesNo(CAN_VIEW);
            String strModifyPermission = validatorMenu.validateYesNo(CAN_MODIFY);

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
    public void remove(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID);

        String strUserGroupId = dtoInput.getStringValue(ID);

        List<Long> listUserGroupId = new ArrayList<>();

        if (StringCheck.isJSONArray(strUserGroupId)) {

            JSONArray arr = (JSONArray) JSONValue.parse(strUserGroupId);

            for (Object id : arr) {
                String strId = String.valueOf(id);
                if (!StringCheck.isNumber(strId)) {
                    throw new DataException(Errors.INVALID_NUMBER, new Object[] {ID});
                }
                listUserGroupId.add(Long.valueOf(strId));
            }

        } else {

            if (!StringCheck.isNumber(strUserGroupId)) {
                throw new DataException(Errors.INVALID_NUMBER, new Object[] {ID});
            }
            listUserGroupId.add(Long.valueOf(strUserGroupId));
        }

        List<UserGroupEntity> listUserGroup = new ArrayList<>();
        
        for (Long userGroupId : listUserGroupId) {

            UserGroupEntity findUserGroupById = userGroupRepository.findOneByIdAndDeleted(userGroupId, CommonConstants.NO);
            if (findUserGroupById == null) {
                throw new DataException(Errors.USER_GROUP_NOT_FOUND);
            }

            // Count users in this group
            Long countUsers = userRepository.countByUserGroupAndDeleted(findUserGroupById, CommonConstants.NO);
            
            log.debug("User group {}, count users: {}", userGroupId, countUsers);
            
            if (countUsers > 0) {
                throw new DataException(Errors.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, 
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
