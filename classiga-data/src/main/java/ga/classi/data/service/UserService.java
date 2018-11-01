/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.service;

import static ga.classi.commons.constant.RequestDataConstants.ACTIVE;
import static ga.classi.commons.constant.RequestDataConstants.AVATAR;
import static ga.classi.commons.constant.RequestDataConstants.EMAIL;
import static ga.classi.commons.constant.RequestDataConstants.FULL_NAME;
import static ga.classi.commons.constant.RequestDataConstants.ID;
import static ga.classi.commons.constant.RequestDataConstants.IP_ADDRESS;
import static ga.classi.commons.constant.RequestDataConstants.NEW_PASSWORD;
import static ga.classi.commons.constant.RequestDataConstants.NEW_PASSWORD_CONFIRM;
import static ga.classi.commons.constant.RequestDataConstants.OLD_PASSWORD;
import static ga.classi.commons.constant.RequestDataConstants.PASSWORD;
import static ga.classi.commons.constant.RequestDataConstants.SEARCH_TERM;
import static ga.classi.commons.constant.RequestDataConstants.TOKEN;
import static ga.classi.commons.constant.RequestDataConstants.USERNAME;
import static ga.classi.commons.constant.RequestDataConstants.USER_AGENT;
import static ga.classi.commons.constant.RequestDataConstants.USER_GROUP_ID;
import static ga.classi.commons.constant.RequestDataConstants.USER_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.data.DTO;
import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import ga.classi.commons.utility.StringCheck;
import ga.classi.data.entity.UserEntity;
import ga.classi.data.entity.UserGroupEntity;
import ga.classi.data.entity.UserSessionEntity;
import ga.classi.data.helper.DataValidator;
import ga.classi.data.repository.UserGroupRepository;
import ga.classi.data.repository.UserRepository;
import ga.classi.data.repository.UserSessionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService extends AbstractServiceHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Transactional(readOnly = true)
    public DTO getAll(DTO dtoInput) {

        String searchTerm = dtoInput.getStringValue(SEARCH_TERM);

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
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(USER_GROUP_ID);

        // Validate values
        String strUserGroupId = validator.validateNumber(USER_GROUP_ID);

        UserGroupEntity userGroup = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strUserGroupId), CommonConstants.NO);
        if (userGroup == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }
        
        List<UserEntity> list = userRepository.findByUserGroup(userGroup);

        return buildResultByEntityList(list);
    }

    @Transactional(readOnly = true)
    public DTO getOne(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID);

        // Validate values
        String strUserId = validator.validateNumber(ID);

        UserEntity user = userRepository.findOneByIdAndDeleted(Long.valueOf(strUserId), CommonConstants.NO);
        if (user == null) {
            throw new DataException(Errors.USER_NOT_FOUND);
        }

        return buildResultByEntity(user);
    }

    @Transactional(readOnly = true)
    public DTO login(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(USERNAME, PASSWORD);

        String strPassword = dtoInput.get(PASSWORD);
        String strUsername = dtoInput.get(USERNAME);
        String strIpAddress = dtoInput.get(IP_ADDRESS);
        String strUserAgent = dtoInput.get(USER_AGENT);

        UserEntity loginUser = userRepository.findOneByLowerEmailOrLowerUsernameAndDeleted(strUsername.toLowerCase(), strUsername.toLowerCase(), CommonConstants.NO);
        if (loginUser == null) {
            throw new DataException(Errors.USER_NOT_FOUND);
        }

        String stirredPassword = DigestUtils.sha256Hex(strPassword + loginUser.getSalt());

        if (!loginUser.getPasswordHash().equals(stirredPassword)) {
            throw new DataException(Errors.USER_NOT_FOUND);
        }

        if (CommonConstants.NO.equals(loginUser.getActive())) {
            throw new DataException(Errors.CANT_LOGIN_CAUSE_USER_NOT_ACTIVE);
        }

        UserGroupEntity userGroup = loginUser.getUserGroup();

        if (CommonConstants.NO.equals(userGroup.getActive())) {
            throw new DataException(Errors.CANT_LOGIN_CAUSE_USER_GROUP_NOT_ACTIVE);
        }

        String token = DigestUtils.sha1Hex(RandomStringUtils.random(16) + System.currentTimeMillis());

        UserSessionEntity userSession = new UserSessionEntity();
        userSession.setUser(loginUser);
        userSession.setToken(token);
        userSession.setIpAddress(strIpAddress);
        userSession.setUserAgent(strUserAgent);
        userSession = userSessionRepository.save(userSession);

        return buildResultByDTO(new DTO().put(TOKEN, token));
    }

    @Transactional(readOnly = true)
    public DTO getByEmail(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(EMAIL);

        // Validate values
        String strEmail = validator.validateEmail(EMAIL);

        UserEntity user = userRepository.findOneByLowerEmailAndDeleted(strEmail.toLowerCase(), CommonConstants.NO);
        if (user == null) {
            throw new DataException(Errors.USER_NOT_FOUND, new Object[]{strEmail});
        }

        return buildResultByEntity(user);
    }

    @Transactional(readOnly = true)
    public DTO getByUsername(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(USERNAME);

        // Validate values
        String strUsername = validator.validateUsername(USERNAME);

        UserEntity user = userRepository.findOneByLowerUsernameAndDeleted(strUsername.toLowerCase(), CommonConstants.NO);
        if (user == null) {
            throw new DataException(Errors.USER_NOT_FOUND, new Object[]{strUsername});
        }

        return buildResultByEntity(user);
    }

    @Transactional
    public DTO add(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(FULL_NAME, EMAIL, USERNAME, PASSWORD, ACTIVE, USER_GROUP_ID);

        // Validate values
        String strFullName = validator.validateEmptyString(FULL_NAME);
        String strPassword = validator.validateEmptyString(PASSWORD);
        String strEmail = validator.validateEmail(EMAIL);
        String strUsername = validator.validateUsername(USERNAME);
        String strUserGroupId = validator.validateNumber(USER_GROUP_ID);
        String strActive = validator.validateYesNo(ACTIVE);

        // Find user by username
        UserEntity userByUsername = userRepository.findOneByLowerUsernameAndDeleted(strUsername.toLowerCase(), CommonConstants.NO);
        if (userByUsername != null) {
            throw new DataException(Errors.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{strUsername});
        }

        // Find user by email
        UserEntity userByEmail = userRepository.findOneByLowerEmailAndDeleted(strEmail.toLowerCase(), CommonConstants.NO);
        if (userByEmail != null) {
            throw new DataException(Errors.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{strEmail});
        }

        // Find user group by ID
        UserGroupEntity userGroupById = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strUserGroupId), CommonConstants.NO);
        if (userGroupById == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }

        String salt = RandomStringUtils.randomAlphanumeric(32);
        String stirredPassword = DigestUtils.sha256Hex(strPassword + salt);

        UserEntity addUser = new UserEntity();
        addUser.setFullName(strFullName);
        addUser.setEmail(strEmail);
        addUser.setUsername(strUsername);
        addUser.setPasswordHash(stirredPassword);
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
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID, FULL_NAME, EMAIL, USERNAME, ACTIVE, USER_GROUP_ID);

        // Validate values
        String strId = validator.validateNumber(ID);
        String strFullName = validator.validateEmptyString(FULL_NAME);
        String strEmail = validator.validateEmail(EMAIL);
        String strUsername = validator.validateUsername(USERNAME);
        String strUserGroupId = validator.validateNumber(USER_GROUP_ID);
        String strActive = validator.validateYesNo(ACTIVE);

        String strPassword = dtoInput.get(PASSWORD);
        String strAvatar = dtoInput.get(AVATAR);

        UserEntity findUserById = userRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
        if (findUserById == null) {
            throw new DataException(Errors.USER_NOT_FOUND);
        }

        // Find other user by username
        UserEntity userByUsername = userRepository.findOneByLowerUsernameAndDeleted(strUsername.toLowerCase(), CommonConstants.NO);
        if (userByUsername != null && !Objects.equals(userByUsername.getId(), Long.valueOf(strId))) {
            throw new DataException(Errors.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{strUsername});
        }

        // Find other user by email
        UserEntity userByEmail = userRepository.findOneByLowerEmailAndDeleted(strEmail.toLowerCase(), CommonConstants.NO);
        if (userByEmail != null && !Objects.equals(userByEmail.getId(), Long.valueOf(strId))) {
            throw new DataException(Errors.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{strEmail});
        }

        // Find user group by ID
        UserGroupEntity userGroupById = userGroupRepository.findOneByIdAndDeleted(Long.valueOf(strUserGroupId), CommonConstants.NO);
        if (userGroupById == null) {
            throw new DataException(Errors.USER_GROUP_NOT_FOUND);
        }

        findUserById.setFullName(strFullName);
        findUserById.setUsername(strUsername);
        findUserById.setEmail(strEmail);
        findUserById.setActive(strActive.toLowerCase());
        findUserById.setUserGroup(userGroupById);
        findUserById.setLowerEmail(strEmail.toLowerCase());
        findUserById.setLowerUsername(strUsername.toLowerCase());
        findUserById.setLowerFullName(strFullName.toLowerCase());
        
        if ((strAvatar != null) && !strAvatar.isEmpty()) {
            findUserById.setAvatar(strAvatar); 
        }
        
        if ((strPassword != null) && !strPassword.trim().isEmpty()) {
            String newSalt = RandomStringUtils.randomAlphanumeric(32);
            String stirredPassword = DigestUtils.sha256Hex(strPassword + newSalt);
            findUserById.setSalt(newSalt);
            findUserById.setPasswordHash(stirredPassword);
        }

        UserEntity updated = userRepository.save(findUserById);

        return buildResultByEntity(updated);
    }

    @Transactional
    public void remove(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID);

        // Validate values
        String strUserId = dtoInput.getStringValue(ID);

        List<UserEntity> listUser = new ArrayList<>();

        if (StringCheck.isJSONArray(strUserId)) {

            JSONArray arr = (JSONArray) JSONValue.parse(strUserId);

            for (Object id : arr) {
                String strId = String.valueOf(id);
                if (!StringCheck.isNumber(strId)) {
                    throw new DataException(Errors.INVALID_NUMBER, new Object[] {ID});
                }
                UserEntity findUserById = userRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
                if (findUserById == null) {
                    throw new DataException(Errors.USER_NOT_FOUND);
                }
                findUserById.setDeleted();
                listUser.add(findUserById);
            }

        } else {

            if (!StringCheck.isNumber(strUserId)) {
                throw new DataException(Errors.INVALID_NUMBER, new Object[] {ID});
            }
            UserEntity findUserById = userRepository.findOneByIdAndDeleted(Long.valueOf(strUserId), CommonConstants.NO);
            if (findUserById == null) {
                throw new DataException(Errors.USER_NOT_FOUND);
            }
            findUserById.setDeleted();
            listUser.add(findUserById);
        }

        List<UserEntity> updated = userRepository.save(listUser);
        log.debug("Updated: {} users", updated.size());
    }

    @Transactional
    public DTO changePassword(DTO dtoInput) {

        // Validate dtoInput
        DataValidator validator = new DataValidator(dtoInput);
        validator.containsRequiredData(ID, OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD_CONFIRM);

        String strId = validator.validateNumber(USER_ID);
        String strOldPassword = validator.validateEmptyString(OLD_PASSWORD);
        String[] passwords = validator.validateEquals(NEW_PASSWORD, NEW_PASSWORD_CONFIRM);
        
        UserEntity user = userRepository.findOneByIdAndDeleted(Long.valueOf(strId), CommonConstants.NO);
        if (user == null) {
            throw new DataException(Errors.USER_NOT_FOUND);
        }

        String oldPasswordHash = DigestUtils.sha256Hex(strOldPassword + user.getSalt());
        
        validator.validateEquals(oldPasswordHash, user.getPasswordHash(), "Old Password");

        String salt = RandomStringUtils.randomAlphanumeric(32);
        String newPasswordHash = DigestUtils.sha256Hex(passwords[0] + salt);
        
        user.setSalt(salt);
        user.setPasswordHash(newPasswordHash);

        UserEntity updated = userRepository.save(user);
        
        return buildResultByEntity(updated);
    }

}
