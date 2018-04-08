package ga.classi.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.data.helper.DTOUtils;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.PasswordUtils;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.repository.UserGroupRepository;
import ga.classi.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService extends AbstractServiceHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Transactional(readOnly = true)
    public DTO getAll(DTO dtoInput) {

        String searchTerm = dtoInput.getStringValue("searchTerm");

        PageRequest pageRequest = createPageRequest(dtoInput, Direction.ASC, "fullName");
        
        Page<UserEntity> pages;

        if (searchTerm == null || searchTerm.isEmpty()) {
            pages = userRepository.findByDeletedFetchUserGroup(CommonConstants.NO, pageRequest);
        } else {
            pages = userRepository.findByDeletedFetchUserGroupSearch(searchTerm.toLowerCase(), CommonConstants.NO, pageRequest);
        }
        
        return buildResultByPage(pages);
    }

    @Transactional(readOnly = true)
    public DTO getAllByUserGroupId(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroupId");

        // Validate values
        String strUserGroupId = dtoInput.getStringValue("userGroupId");
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");

        UserGroupEntity userGroup = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strUserGroupId), CommonConstants.NO);
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        
        List<UserEntity> list = userRepository.findByUserGroup(userGroup);

        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public DTO getOne(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        // Validate values
        String strUserId = dtoInput.getStringValue("id");
        DataValidation.validateNumeric(strUserId, "User ID");

        UserEntity user = userRepository.findOneByIdAndDeleted(Long.valueOf(strUserId), CommonConstants.NO);
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        return buildResultByEntity(user);
    }

    @Transactional(readOnly = true)
    public DTO login(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "password", "username");

        String strPassword = dtoInput.get("password");
        String strUsername = dtoInput.get("username");

        UserEntity loginUser = userRepository.findOneByLowerEmailOrLowerUsernameAndDeleted(strUsername.toLowerCase(), strUsername.toLowerCase(), CommonConstants.NO);
        if (loginUser == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        String stirredPassword = PasswordUtils.stir(strPassword, loginUser.getSalt());

        if (!loginUser.getPassword().equals(stirredPassword)) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        if (CommonConstants.NO.equals(loginUser.getActive())) {
            throw new DataException(ExceptionCode.E1008, ErrorMessageConstants.CANT_LOGIN_CAUSE_USER_NOT_ACTIVE);
        }

        UserGroupEntity userGroup = loginUser.getUserGroup();

        if (CommonConstants.NO.equals(userGroup.getActive())) {
            throw new DataException(ExceptionCode.E1008, ErrorMessageConstants.CANT_LOGIN_CAUSE_USER_GROUP_NOT_ACTIVE);
        }

        List<DTO> menuPermissions = DTOUtils.toDTOList(userGroup.getMenuPermissions(), "userGroup");
        DTO dtoUserGroup = DTOUtils.toDTO(userGroup).put("menuPermissions", menuPermissions);
        DTO dtoUser = DTOUtils.toDTO(loginUser).put("userGroup", dtoUserGroup);

        return buildResultByDto(dtoUser);
    }

    @Transactional(readOnly = true)
    public DTO getByEmail(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "email");

        String strEmail = dtoInput.get("email");

        // Validate values
        DataValidation.validateEmail(strEmail);

        UserEntity user = userRepository.findOneByLowerEmailAndDeleted(strEmail.toLowerCase(), CommonConstants.NO);
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{strEmail});
        }

        return buildResultByEntity(user);
    }

    @Transactional(readOnly = true)
    public DTO getByUsername(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "username");

        String strUsername = dtoInput.get("username");

        // Validate values
        DataValidation.validateUsername(strUsername);

        UserEntity user = userRepository.findOneByLowerUsernameAndDeleted(strUsername.toLowerCase(), CommonConstants.NO);
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{strUsername});
        }

        return buildResultByEntity(user);
    }

    @Transactional
    public DTO add(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "fullName", "email", "username", "password", "active", "userGroupId");

        String strFullName = dtoInput.get("fullName");
        String strEmail = dtoInput.get("email");
        String strUsername = dtoInput.get("username");
        String strPassword = dtoInput.get("password");
        String strActive = dtoInput.get("active");
        String strUserGroupId = dtoInput.getStringValue("userGroupId");

        // Validate values
        DataValidation.validateEmpty(strFullName, "Full Name");
        DataValidation.validateEmpty(strPassword, "Password");
        DataValidation.validateEmail(strEmail);
        DataValidation.validateUsername(strUsername);
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        DataValidation.validateYesNo(strActive, "Active");

        // Find user by username
        UserEntity userByUsername = userRepository.findOneByLowerUsernameAndDeleted(strUsername.toLowerCase(), CommonConstants.NO);
        if (userByUsername != null) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{strUsername});
        }

        // Find user by email
        UserEntity userByEmail = userRepository.findOneByLowerEmailAndDeleted(strEmail.toLowerCase(), CommonConstants.NO);
        if (userByEmail != null) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{strEmail});
        }

        // Find user group by ID
        UserGroupEntity userGroupById = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strUserGroupId), CommonConstants.NO);
        if (userGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        String salt = RandomStringUtils.randomAlphanumeric(32);
        String stirredPassword = PasswordUtils.stir(strPassword, salt);

        UserEntity addUser = new UserEntity();
        addUser.setFullName(strFullName);
        addUser.setEmail(strEmail);
        addUser.setUsername(strUsername);
        addUser.setPassword(stirredPassword);
        addUser.setSalt(salt);
        addUser.setActive(strActive.toLowerCase());
        addUser.setUserGroup(userGroupById);
        addUser.setLowerFullName(strFullName.toLowerCase());
        addUser.setLowerUsername(strUsername.toLowerCase());
        addUser.setLowerEmail(strEmail.toLowerCase());

        UserEntity added = userRepository.save(addUser);

        return buildResultByEntity(added);
    }

    @Transactional
    public DTO edit(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id", "fullName", "email", "username", "active", "userGroupId");

        String strId = dtoInput.getStringValue("id");
        String strFullName = dtoInput.get("fullName");
        String strEmail = dtoInput.get("email");
        String strUsername = dtoInput.get("username");
        String strPassword = dtoInput.get("password");
        String strActive = dtoInput.get("active");
        String strUserGroupId = dtoInput.getStringValue("userGroupId");

        // Validate values
        DataValidation.validateNumeric(strId, "User ID");
        DataValidation.validateEmpty(strFullName, "First Name");
        DataValidation.validateEmail(strEmail);
        DataValidation.validateUsername(strUsername);
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        DataValidation.validateYesNo(strActive, "Active");

        UserEntity findUserById = userRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
        if (findUserById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        // Find other user by username
        UserEntity userByUsername = userRepository.findOneByLowerUsernameAndDeleted(strUsername.toLowerCase(), CommonConstants.NO);
        if (userByUsername != null && !Objects.equals(userByUsername.getId(), Long.valueOf(strId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{strUsername});
        }

        // Find other user by email
        UserEntity userByEmail = userRepository.findOneByLowerEmailAndDeleted(strEmail.toLowerCase(), CommonConstants.NO);
        if (userByEmail != null && !Objects.equals(userByEmail.getId(), Long.valueOf(strId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{strEmail});
        }

        // Find user group by ID
        UserGroupEntity userGroupById = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strUserGroupId), CommonConstants.NO);
        if (userGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        findUserById.setFullName(strFullName);
        findUserById.setUsername(strUsername);
        findUserById.setEmail(strEmail);
        findUserById.setActive(strActive.toLowerCase());
        findUserById.setUserGroup(userGroupById);
        findUserById.setLowerEmail(strEmail.toLowerCase());
        findUserById.setLowerUsername(strUsername.toLowerCase());
        findUserById.setLowerFullName(strFullName.toLowerCase());
        
        if ((strPassword != null) && !strPassword.trim().isEmpty()) {
            String newSalt = RandomStringUtils.randomAlphanumeric(32);
            String stirredPassword = PasswordUtils.stir(strPassword, newSalt);
            findUserById.setSalt(newSalt);
            findUserById.setPassword(stirredPassword);
        }

        UserEntity updated = userRepository.save(findUserById);

        return buildResultByEntity(updated);
    }

    @Transactional
    public void remove(DTO dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        // Validate values
        String strUserId = dtoInput.getStringValue("id");

        List<UserEntity> listUser = new ArrayList<UserEntity>();

        if (StringCheck.isJSONArray(strUserId)) {

            JSONArray arr = (JSONArray) JSONValue.parse(strUserId);

            for (Object id : arr) {
                String strId = String.valueOf(id);
                DataValidation.validateNumeric(strId, "User ID");
                UserEntity findUserById = userRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
                if (findUserById == null) {
                    throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
                }
                findUserById.setDeleted();
                listUser.add(findUserById);
            }

        } else {

            DataValidation.validateNumeric(strUserId, "User ID");
            UserEntity findUserById = userRepository.findOneByIdAndDeleted(Long.valueOf(strUserId), CommonConstants.NO);
            if (findUserById == null) {
                throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
            }
            findUserById.setDeleted();
            listUser.add(findUserById);
        }

        List<UserEntity> updated = userRepository.save(listUser);
        log.debug("Updated: {} users", updated.size());
    }

}
