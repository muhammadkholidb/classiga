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

import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.PasswordUtils;
import ga.classi.commons.helper.StringCheck;
import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.error.DataException;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.error.ExceptionCode;
import ga.classi.data.helper.DataValidation;
import ga.classi.data.helper.Dto;
import ga.classi.data.helper.DtoUtils;
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
    public Dto getAllUserWithGroup(Dto dtoInput) {

        String searchTerm = dtoInput.getStringValue("searchTerm");

        PageRequest pageRequest = createPageRequest(dtoInput, Direction.ASC, "firstName");
        
        Page<UserEntity> pages;

        if (searchTerm == null || searchTerm.isEmpty()) {
            log.debug("Not filtered ...");
            pages = userRepository.findAllFetchUserGroup(pageRequest);
        } else {
            log.debug("Filtered ...");
            pages = userRepository.findAllFetchUserGroupFiltered(searchTerm.toLowerCase(), pageRequest);
        }
        
        return buildResultByPage(pages);
    }

    @Transactional(readOnly = true)
    public Dto getUserListByUserGroupId(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroupId");

        // Validate values
        String strUserGroupId = dtoInput.getStringValue("userGroupId");
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");

        List<UserEntity> list = userRepository.findByUserGroup(new UserGroupEntity(Long.valueOf(strUserGroupId)));

        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public Dto getUserById(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        // Validate values
        String strUserId = dtoInput.getStringValue("id");
        DataValidation.validateNumeric(strUserId, "User ID");

        UserEntity user = userRepository.findOne(Long.valueOf(strUserId));
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        return buildResultByEntity(user);
    }

    @Transactional(readOnly = true)
    public Dto login(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "password", "username");

        String strPassword = dtoInput.get("password");
        String strUsername = dtoInput.get("username");

        UserEntity loginUser = userRepository.findOneByLowerEmailOrLowerUsername(strUsername.toLowerCase(), strUsername.toLowerCase());
        if (loginUser == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        String stirredPassword = PasswordUtils.stirWithSalt(strPassword, loginUser.getSalt());

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

        List<Dto> menuPermissions = DtoUtils.toDtoList(userGroup.getMenuPermissions(), "userGroup");
        Dto dtoUserGroup = DtoUtils.toDto(userGroup).put("menuPermissions", menuPermissions);
        Dto dtoUser = DtoUtils.toDto(loginUser).put("userGroup", dtoUserGroup);

        return buildResultByDto(dtoUser);
    }

    @Transactional(readOnly = true)
    public Dto getUserByEmail(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "email");

        String strEmail = dtoInput.get("email");

        // Validate values
        DataValidation.validateEmail(strEmail);

        UserEntity user = userRepository.findOneByLowerEmail(strEmail.toLowerCase());
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{strEmail});
        }

        return buildResultByEntity(user);
    }

    @Transactional(readOnly = true)
    public Dto getUserByUsername(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "username");

        String strUsername = dtoInput.get("username");

        // Validate values
        DataValidation.validateUsername(strUsername);

        UserEntity user = userRepository.findOneByLowerUsername(strUsername.toLowerCase());
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{strUsername});
        }

        return buildResultByEntity(user);
    }

    @Transactional
    public Dto addUser(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "firstName", "email", "username", "password", "active", "userGroupId");

        String strFirstName = dtoInput.get("firstName");
        String strLastName = dtoInput.get("lastName");
        String strEmail = dtoInput.get("email");
        String strUsername = dtoInput.get("username");
        String strPassword = dtoInput.get("password");
        String strActive = dtoInput.get("active");
        String strUserGroupId = dtoInput.getStringValue("userGroupId");

        // Validate values
        DataValidation.validateEmpty(strFirstName, "First Name");
        DataValidation.validateEmpty(strPassword, "Password");
        DataValidation.validateEmail(strEmail);
        DataValidation.validateUsername(strUsername);
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        DataValidation.validateYesNo(strActive, "Active");

        // Find user by username
        UserEntity userByUsername = userRepository.findOneByLowerUsername(strUsername.toLowerCase());
        if (userByUsername != null) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{strUsername});
        }

        // Find user by email
        UserEntity userByEmail = userRepository.findOneByLowerEmail(strEmail.toLowerCase());
        if (userByEmail != null) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{strEmail});
        }

        // Find user group by ID
        UserGroupEntity userGroupById = userGroupRepository.findOne(Long.valueOf(strUserGroupId));
        if (userGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        String salt = RandomStringUtils.randomAlphanumeric(32);
        String stirredPassword = PasswordUtils.stirWithSalt(strPassword, salt);

        UserEntity addUser = new UserEntity();
        addUser.setFirstName(strFirstName);
        addUser.setLastName(strLastName);
        addUser.setEmail(strEmail);
        addUser.setUsername(strUsername);
        addUser.setPassword(stirredPassword);
        addUser.setSalt(salt);
        addUser.setActive(strActive.toLowerCase());
        addUser.setUserGroup(userGroupById);
        addUser.setLowerUsername(strUsername.toLowerCase());
        addUser.setLowerEmail(strEmail.toLowerCase());

        UserEntity added = userRepository.save(addUser);

        return buildResultByEntity(added);
    }

    @Transactional
    public Dto editUser(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id", "firstName", "email", "username", "active", "userGroupId");

        String strId = dtoInput.getStringValue("id");
        String strFirstName = dtoInput.get("firstName");
        String strLastName = dtoInput.get("lastName");
        String strEmail = dtoInput.get("email");
        String strUsername = dtoInput.get("username");
        String strPassword = dtoInput.get("password");
        String strActive = dtoInput.get("active");
        String strUserGroupId = dtoInput.getStringValue("userGroupId");

        // Validate values
        DataValidation.validateNumeric(strId, "User ID");
        DataValidation.validateEmpty(strFirstName, "First Name");
        DataValidation.validateEmail(strEmail);
        DataValidation.validateUsername(strUsername);
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        DataValidation.validateYesNo(strActive, "Active");

        UserEntity findUserById = userRepository.findOne(Long.valueOf(strId));
        if (findUserById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }

        // Find other user by username
        UserEntity userByUsername = userRepository.findOneByLowerUsername(strUsername.toLowerCase());
        if (userByUsername != null && !Objects.equals(userByUsername.getId(), Long.valueOf(strId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{strUsername});
        }

        // Find other user by email
        UserEntity userByEmail = userRepository.findOneByLowerEmail(strEmail.toLowerCase());
        if (userByEmail != null && !Objects.equals(userByEmail.getId(), Long.valueOf(strId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{strEmail});
        }

        // Find user group by ID
        UserGroupEntity userGroupById = userGroupRepository.findOne(Long.valueOf(strUserGroupId));
        if (userGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        findUserById.setFirstName(strFirstName);
        findUserById.setLastName(strLastName);
        findUserById.setUsername(strUsername);
        findUserById.setEmail(strEmail);
        findUserById.setActive(strActive.toLowerCase());
        findUserById.setUserGroup(userGroupById);
        findUserById.setLowerEmail(strEmail.toLowerCase());
        findUserById.setLowerUsername(strUsername.toLowerCase());

        if ((strPassword != null) && !strPassword.trim().isEmpty()) {
            String newSalt = RandomStringUtils.randomAlphanumeric(32);
            String stirredPassword = PasswordUtils.stirWithSalt(strPassword, newSalt);
            findUserById.setSalt(newSalt);
            findUserById.setPassword(stirredPassword);
        }

        UserEntity updated = userRepository.save(findUserById);

        return buildResultByEntity(updated);
    }

    @Transactional
    public void removeUser(Dto dtoInput) {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");

        // Validate values
        String strUserId = dtoInput.getStringValue("id");

        List<Long> listUserId = new ArrayList<Long>();

        if (StringCheck.isJSONArray(strUserId)) {

            JSONArray arr = (JSONArray) JSONValue.parse(strUserId);

            for (Object id : arr) {
                String strId = String.valueOf(id);
                DataValidation.validateNumeric(strId, "User ID");
                listUserId.add(Long.valueOf(strId));
            }

        } else {

            DataValidation.validateNumeric(strUserId, "User ID");
            listUserId.add(Long.valueOf(strUserId));
        }

        List<UserEntity> deleted = userRepository.deleteByIdIn(listUserId);

        log.debug("Deleted: {} users", deleted.size());
    }

}
