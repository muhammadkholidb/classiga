package ga.classi.data.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

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

import ga.classi.commons.data.helper.Dto;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.test.DefaultSpringTestDbUnitConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
@TestExecutionListeners({ 
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("MenuPermissionServiceTest.xml")
@DatabaseTearDown("MenuPermissionServiceTestCleanup.xml")
public class MenuPermissionServiceTest extends DefaultSpringTestDbUnitConfiguration {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Test
    public void testGetAllMenuPermissions() {
        log.debug("Test get all menu permission ...");
        Dto result = menuPermissionService.getAllMenuPermissions(null);
        log.debug("Result: {}", result);
        List<Dto> list = result.get(CommonConstants.CONTENT);
        assertEquals(8, list.size());
    }

    @Test
    public void testGetMenuPermissionListByUserGroupId() {
        log.debug("Test get menu permission list by user group id ...");
        Dto dtoInput = new Dto();
        dtoInput.put("userGroupId", 1L);
        try {
            Dto result = menuPermissionService.getMenuPermissionListByUserGroupId(dtoInput);
            log.debug("Result: {}", result);
            List<Dto> list = result.get(CommonConstants.CONTENT);
            assertEquals(4, list.size());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

}
