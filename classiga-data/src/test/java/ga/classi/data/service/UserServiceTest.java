package ga.classi.data.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.test.DefaultSpringTestDbUnitConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
@TestExecutionListeners({ 
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("UserServiceTest.xml")
@DatabaseTearDown("UserServiceTestCleanup.xml")
public class UserServiceTest extends DefaultSpringTestDbUnitConfiguration {

    @Autowired
    private UserService userService;

    @Test
    public void testGetAllUser() {
        log.debug("Test get all user ...");
        Dto dtoInput = new Dto();
        dtoInput.put("searchTerm", "yahoo");
        try {
            Dto result = userService.getAll(dtoInput);
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
            List<Dto> list = userService.getAllByUserGroupId(dtoInput).get(CommonConstants.CONTENT);
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
            Dto dtoUser = userService.getOne(dtoInput).get(CommonConstants.CONTENT);
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
            userService.getOne(dtoInput);
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
            Dto dtoUser = userService.getByEmail(dtoInput).get(CommonConstants.CONTENT);
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
            userService.getByEmail(dtoInput);
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
            Dto dtoUser = userService.getByUsername(dtoInput).get(CommonConstants.CONTENT);
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
            userService.getByUsername(dtoInput);
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
            Dto result = userService.add(dtoInput).get(CommonConstants.CONTENT);
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
            userService.add(dtoInput);
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
            Dto result = userService.edit(dtoInput).get(CommonConstants.CONTENT);
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
            userService.edit(dtoInput);
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
            userService.edit(dtoInput);
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
            userService.remove(dtoInput);
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }

        // Find removed user
        try {
            userService.getOne(dtoInput);
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
            userService.remove(dtoInput);
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
        
        for (Object objId : array) {

            // Find removed user
            try {
                userService.getOne(new Dto().put("id", objId));
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
    }

}
