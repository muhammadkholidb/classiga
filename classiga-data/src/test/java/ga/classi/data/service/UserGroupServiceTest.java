package ga.classi.data.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
@DatabaseSetup("UserGroupServiceTest.xml")
@DatabaseTearDown("UserGroupServiceTestCleanup.xml")
public class UserGroupServiceTest extends DefaultSpringTestDbUnitConfiguration {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MenuPermissionService userGroupMenuPermissionService;

    @Test
    public void testGetAllUserGroups() {
        log.debug("Test get all user groups ...");
        List<Dto> result = userGroupService.getAll(new Dto()).get(CommonConstants.CONTENT);
        log.debug("Result: {}", result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindOneUserGroupFail() {
        log.debug("Test fail find one user group ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 123L);
        try {
            userGroupService.getOne(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testFindOneUserGroupSuccess() {
        log.debug("Test success find one user group ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 1L);
        try {
            Dto result = userGroupService.getOne(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", result);
            assertEquals(1L, result.get("id"));
            assertEquals("Administrator", result.get("name"));
            assertEquals(CommonConstants.YES, result.get("active"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testFindOneUserGroupWithMenuPermissions() {
        log.debug("Test success find one user group with menu permissions ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 1L);
        try {
            Dto result = userGroupService.getOneWithMenuPermissions(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", result);

            assertEquals(1L, result.get("id"));
            assertEquals("Administrator", result.get("name"));
            assertEquals(CommonConstants.YES, result.get("active"));

            List<Dto> listDtoMenuPermissions = result.get("menuPermissions");
            assertEquals(4, listDtoMenuPermissions.size());

        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddUserGroupSuccess() {
        log.debug("Test success add user group ...");

        JSONObject userGroup = new JSONObject();
        userGroup.put("name", "Group 1");
        userGroup.put("active", CommonConstants.YES);

        JSONObject menu1 = new JSONObject();
        menu1.put("menuCode", "menu.settings");
        menu1.put("canView", CommonConstants.YES);
        menu1.put("canModify", CommonConstants.NO);

        JSONObject menu2 = new JSONObject();
        menu2.put("menuCode", "menu.settings.system");
        menu2.put("canView", CommonConstants.YES);
        menu2.put("canModify", CommonConstants.NO);

        JSONObject menu3 = new JSONObject();
        menu3.put("menuCode", "menu.settings.user");
        menu3.put("canView", CommonConstants.YES);
        menu3.put("canModify", CommonConstants.NO);

        JSONObject menu4 = new JSONObject();
        menu4.put("menuCode", "menu.settings.usergroup");
        menu4.put("canView", CommonConstants.YES);
        menu4.put("canModify", CommonConstants.NO);

        JSONArray menuPermissions = new JSONArray();
        menuPermissions.add(menu1);
        menuPermissions.add(menu2);
        menuPermissions.add(menu3);
        menuPermissions.add(menu4);

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);

        try {
            Dto result = userGroupService.add(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", result);
            assertEquals("Group 1", result.get("name"));
            assertEquals(CommonConstants.YES, result.get("active"));

            // Find total user group now
            List<Dto> listDtoUserGroup = userGroupService.getAll(new Dto()).get(CommonConstants.CONTENT);
            assertEquals(4, listDtoUserGroup.size());

            // Find list menus for this user group
            List<Dto> listMenuPermissionByUserGroupId = userGroupMenuPermissionService.getMenuPermissionListByUserGroupId(new Dto().put("userGroupId", result.get("id"))).get(CommonConstants.CONTENT);
            assertEquals(menuPermissions.size(), listMenuPermissionByUserGroupId.size());

        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddUserGroupFail() {
        log.debug("Test fail add user group ...");

        JSONObject userGroup = new JSONObject();
        userGroup.put("name", "Administrator");
        userGroup.put("active", CommonConstants.YES);

        JSONArray menuPermissions = new JSONArray();

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);

        try {
            userGroupService.add(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            Assert.fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditUserGroupSuccess() {
        log.debug("Test success edit user group ...");

        JSONObject userGroup = new JSONObject();
        userGroup.put("id", 1L);
        userGroup.put("name", "Super Administrator");
        userGroup.put("active", CommonConstants.NO);

        JSONObject menu1 = new JSONObject();
        menu1.put("menuCode", "menu.settings");
        menu1.put("canView", CommonConstants.YES);
        menu1.put("canModify", CommonConstants.NO);

        JSONObject menu2 = new JSONObject();
        menu2.put("menuCode", "menu.settings.system");
        menu2.put("canView", CommonConstants.YES);
        menu2.put("canModify", CommonConstants.NO);

        JSONObject menu3 = new JSONObject();
        menu3.put("menuCode", "menu.settings.user");
        menu3.put("canView", CommonConstants.YES);
        menu3.put("canModify", CommonConstants.NO);

        JSONObject menu4 = new JSONObject();
        menu4.put("menuCode", "menu.settings.usergroup");
        menu4.put("canView", CommonConstants.YES);
        menu4.put("canModify", CommonConstants.NO);

        JSONArray menuPermissions = new JSONArray();
        menuPermissions.add(menu1);
        menuPermissions.add(menu2);
        menuPermissions.add(menu3);
        menuPermissions.add(menu4);

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);

        try {
            Dto result = userGroupService.edit(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: {}", result);

            Assert.assertEquals(CommonConstants.NO, result.get("active"));
            Assert.assertEquals("Super Administrator", result.get("name"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            Assert.fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditUserGroupFail1() {
        // Test fail cause ID not foud
        log.debug("Test fail #1 edit user group ...");

        JSONObject userGroup = new JSONObject();
        userGroup.put("id", 123L);
        userGroup.put("name", "Administrator");
        userGroup.put("active", CommonConstants.YES);

        JSONArray menuPermissions = new JSONArray();

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);

        try {
            userGroupService.edit(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditUserGroupFail2() {
        // Test fail cause name already exists
        log.debug("Test fail #2 edit user group ...");

        JSONObject userGroup = new JSONObject();
        userGroup.put("id", 1L);
        userGroup.put("name", "User");
        userGroup.put("active", CommonConstants.YES);

        JSONArray menuPermissions = new JSONArray();

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);

        try {
            userGroupService.edit(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserGroupFail() {
        log.debug("Test fail #1 remove user group ...");
        Long userGroupId = 1L;
        try {
            userGroupService.remove(new Dto().put("id", userGroupId));
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1002, e.getCode());
            assertEquals(ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveUserGroupFail2() {
        log.debug("Test fail #2 remove user group ...");

        JSONArray arr = new JSONArray();
        arr.add(3L);
        arr.add(1L);

        try {
            userGroupService.remove(new Dto().put("id", arr.toString()));
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1002, e.getCode());
            assertEquals(ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }

        for (Object objId : arr) {

            // Find user groups, make sure they are not deleted
            try {
                Dto userGroup = userGroupService.getOne(new Dto().put("id", objId)).get(CommonConstants.CONTENT);
                log.debug("Result: {}", userGroup);

                Long id = userGroup.get("id");
                String name = userGroup.get("name");
                String active = userGroup.get("active");
                
                if (id == 1L) {

                    assertEquals("Administrator", name);
                    assertEquals(CommonConstants.YES, active);
                    
                    // Find the user group permission menus, make sure they are not deleted
                    List<Dto> listDtoMenuPermission = userGroupMenuPermissionService.getMenuPermissionListByUserGroupId(new Dto().put("userGroupId", id)).get(CommonConstants.CONTENT);
                    assertEquals(4, listDtoMenuPermission.size());

                } else if (id == 3L) {

                    assertEquals("Marketing", name);
                    assertEquals(CommonConstants.YES, active);
                    
                    // Find the user group permission menus, make sure they are not deleted
                    List<Dto> listDtoMenuPermission = userGroupMenuPermissionService.getMenuPermissionListByUserGroupId(new Dto().put("userGroupId", id)).get(CommonConstants.CONTENT);
                    assertEquals(4, listDtoMenuPermission.size());

                } else {
                    fail("Invalid user group ID");
                }

            } catch (Exception e) {
                log.error(e.toString(), e);
                fail(e.toString());
            }
        }
    }

    @Test
    public void testRemoveUserGroupSuccess() {
        log.debug("Test success remove user group ...");
        Dto dtoInput = new Dto().put("id", 3L);
        try {
            userGroupService.remove(dtoInput);
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }

        // Find removed user group
        try {
            userGroupService.getOne(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }

    }

}
