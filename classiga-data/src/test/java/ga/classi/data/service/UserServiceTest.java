package ga.classi.data.service;

import ga.classi.data.service.UserService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.error.DataException;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.error.ExceptionCode;
import ga.classi.data.helper.Dto;
import ga.classi.data.test.AbstractTestDataImport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
public class UserServiceTest extends AbstractTestDataImport {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Before
    public void init() throws Exception {
        setDataSets("dataset/test-user.dataset.xml");
    }

    @After
    public void finish() throws Exception {
        log.debug("Test done, clearing data ...");
        clearDataSets();
    }

    @Test
    public void testGetAllUser() {
        log.debug("Test get all user ...");
        Dto dtoInput = new Dto();
        dtoInput.put("searchTerm", "yahoo");
        try {
            Dto result = userService.getAllUserWithGroup(dtoInput);
            log.debug("Result: {}", result);
            List<Dto> list = result.get(CommonConstants.CONTENT);
            assertEquals(2, list.size());
            for (Dto dto : list) {
                Long id = dto.get("id");
                Dto dtoUserGroup = dto.getDto("userGroup");
                if (id.equals(1L)) {
                    assertEquals("John", dto.get("fullName"));
                    assertEquals("johndoe", dto.get("username"));
                    assertEquals("johndoe@yahoo.com", dto.get("email"));
                    assertEquals(CommonConstants.YES, dto.get("active"));
                    assertEquals(1L, dtoUserGroup.get("id"));
                    assertEquals("Administrator", dtoUserGroup.get("name"));
                } else if (id.equals(2L)) {
                    assertEquals("Fulan", dto.get("fullName"));
                    assertEquals("fulan", dto.get("username"));
                    assertEquals("fulan@yahoo.com", dto.get("email"));
                    assertEquals(CommonConstants.YES, dto.get("active"));
                    assertEquals(2L, dtoUserGroup.get("id"));
                    assertEquals("User", dtoUserGroup.get("name"));
                }
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetUserListByUserGroupId() {
        log.debug("Test get user list by user group id ...");
        Dto dtoInput = new Dto();
        dtoInput.put("userGroupId", 1L);
        try {
            List<Dto> list = userService.getUserListByUserGroupId(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", list);
            assertEquals(1, list.size());
            Dto dtoUser = list.get(0);
            Dto dtoUserGroup = dtoUser.getDto("userGroup");
            assertEquals("John", dtoUser.get("fullName"));
            assertEquals("johndoe", dtoUser.get("username"));
            assertEquals("johndoe@yahoo.com", dtoUser.get("email"));
            assertEquals(CommonConstants.YES, dtoUser.get("active"));
            assertEquals(1L, dtoUserGroup.get("id"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testGetUserByIdSuccess() {
        log.debug("Test success get user by ID ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 1L);
        try {
            Dto dtoUser = userService.getUserById(dtoInput).get(CommonConstants.CONTENT);
            Dto dtoUserGroup = dtoUser.getDto("userGroup");
            assertEquals(1L, dtoUser.get("id"));
            assertEquals("John", dtoUser.get("fullName"));
            assertEquals("johndoe", dtoUser.get("username"));
            assertEquals("johndoe@yahoo.com", dtoUser.get("email"));
            assertEquals(CommonConstants.YES, dtoUser.get("active"));
            assertEquals(1L, dtoUserGroup.get("id"));
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testGetUserByIdFail() {
        log.debug("Test fail get user by ID ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 20L);
        try {
            userService.getUserById(dtoInput);
            fail("User should not be found.");
        } catch (DataException ex) {
            log.debug(ex.toString());
            assertEquals(ExceptionCode.E1001, ex.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testLoginSuccess() {
        log.debug("Test success login ...");
        Dto dtoInput = new Dto();
        dtoInput.put("username", "fulan");
        dtoInput.put("password", "12345678");
        try {
            Dto dtoUser = userService.login(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", dtoUser);
            assertEquals("Fulan", dtoUser.get("fullName"));
            assertEquals("fulan", dtoUser.get("username"));
            assertEquals("fulan@yahoo.com", dtoUser.get("email"));
            assertEquals(CommonConstants.YES, dtoUser.get("active"));

            Dto dtoUserGroup = dtoUser.getDto("userGroup");
            assertEquals(2L, dtoUserGroup.get("id"));
            assertEquals("User", dtoUserGroup.get("name"));

            List<Dto> menuPermissions = dtoUserGroup.get("menuPermissions");
            assertEquals(4, menuPermissions.size());

        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testGetUserByEmailSuccess() {
        log.debug("Test success get user by email  ...");
        Dto dtoInput = new Dto();
        dtoInput.put("email", "johndoe@yahoo.com");
        try {
            Dto dtoUser = userService.getUserByEmail(dtoInput).get(CommonConstants.CONTENT);
            Dto dtoUserGroup = dtoUser.getDto("userGroup");
            assertEquals(1L, dtoUser.get("id"));
            assertEquals("John", dtoUser.get("fullName"));
            assertEquals("johndoe", dtoUser.get("username"));
            assertEquals("johndoe@yahoo.com", dtoUser.get("email"));
            assertEquals(CommonConstants.YES, dtoUser.get("active"));
            assertEquals(1L, dtoUserGroup.get("id"));
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testGetUserByEmailFail() {
        log.debug("Test fail get user by email ...");
        Dto dtoInput = new Dto();
        dtoInput.put("email", "xxxxx@yahoo.com");
        try {
            userService.getUserByEmail(dtoInput);
            fail("User should not found.");
        } catch (DataException ex) {
            log.debug(ex.toString());
            assertEquals(ExceptionCode.E1001, ex.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testGetUserByUsernameSuccess() {
        log.debug("Test success get user by username ...");
        Dto dtoInput = new Dto();
        dtoInput.put("username", "fulan");
        try {
            Dto dtoUser = userService.getUserByUsername(dtoInput).get(CommonConstants.CONTENT);
            Dto dtoUserGroup = dtoUser.getDto("userGroup");
            assertEquals(2L, dtoUser.get("id"));
            assertEquals("Fulan", dtoUser.get("fullName"));
            assertEquals("fulan", dtoUser.get("username"));
            assertEquals("fulan@yahoo.com", dtoUser.get("email"));
            assertEquals(CommonConstants.YES, dtoUser.get("active"));
            assertEquals(2L, dtoUserGroup.get("id"));
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testGetUserByUsernameFail() {
        log.debug("Test fail get user by username ...");
        Dto dtoInput = new Dto();
        dtoInput.put("username", "xxxxx");
        try {
            userService.getUserByUsername(dtoInput);
            fail("User should not found.");
        } catch (DataException ex) {
            log.debug(ex.toString());
            assertEquals(ExceptionCode.E1001, ex.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testAddUserSuccess() {
        log.debug("Test success add user ...");

        Dto dtoInput = new Dto();
        dtoInput.put("fullName", "Brian");
        dtoInput.put("username", "brian");
        dtoInput.put("email", "bryan.mckningt@yahoo.com");
        dtoInput.put("password", "123");
        dtoInput.put("salt", "abc");
        dtoInput.put("active", CommonConstants.YES);
        dtoInput.put("userGroupId", 1L);

        try {
            Dto result = userService.addUser(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", result);
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testAddUserFail() {
        log.debug("Test fail add user ...");

        Dto dtoInput = new Dto();
        dtoInput.put("fullName", "John");
        dtoInput.put("username", "johndoe");
        dtoInput.put("email", "johndoe@yahoo.com");
        dtoInput.put("password", "123");
        dtoInput.put("salt", "abc");
        dtoInput.put("active", CommonConstants.YES);
        dtoInput.put("userGroupId", 1L);

        try {
            userService.addUser(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserSuccess() {
        log.debug("Test success edit user ...");

        Dto dtoInput = new Dto();
        dtoInput.put("id", 2L);
        dtoInput.put("fullName", "Fulan");
        dtoInput.put("username", "fulan2");
        dtoInput.put("email", "fulan2@yahoo.com");
        dtoInput.put("password", "123");
        dtoInput.put("salt", "abc");
        dtoInput.put("active", CommonConstants.NO);
        dtoInput.put("userGroupId", 1L);

        try {
            Dto result = userService.editUser(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", result);
            assertEquals("fulan2", result.get("username"));
            assertEquals("fulan2@yahoo.com", result.get("email"));
            assertEquals(CommonConstants.NO, result.get("active"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserFail1() {
        log.debug("Test fail #1 edit user ...");

        Dto dtoInput = new Dto();
        dtoInput.put("id", 20L);
        dtoInput.put("fullName", "Fulan");
        dtoInput.put("username", "fulan2");
        dtoInput.put("email", "fulan2@yahoo.com");
        dtoInput.put("password", "123");
        dtoInput.put("salt", "abc");
        dtoInput.put("active", CommonConstants.NO);
        dtoInput.put("userGroupId", 1L);

        try {
            userService.editUser(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserFail2() {
        log.debug("Test fail #2 edit user ...");

        Dto dtoInput = new Dto();
        dtoInput.put("id", 2L);
        dtoInput.put("fullName", "John");
        dtoInput.put("username", "johndoe2");
        dtoInput.put("email", "johndoe@yahoo.com");
        dtoInput.put("password", "123");
        dtoInput.put("salt", "abc");
        dtoInput.put("active", CommonConstants.NO);
        dtoInput.put("userGroupId", 1L);

        try {
            userService.editUser(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserSuccess() {
        log.debug("Test success remove user ...");

        Dto dtoInput = new Dto();
        dtoInput.put("id", 2L);

        try {
            userService.removeUser(dtoInput);
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveUserSuccess2() {
        log.debug("Test success remove user 2 (array) ...");

        JSONArray array = new JSONArray();
        array.add(1);
        array.add(2);

        Dto dtoInput = new Dto();
        dtoInput.put("id", array.toString());

        try {
            userService.removeUser(dtoInput);
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

}
