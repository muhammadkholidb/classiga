package ga.classi.data.service;

import ga.classi.data.service.MenuPermissionService;
import ga.classi.commons.helper.CommonConstants;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ga.classi.data.helper.Dto;
import ga.classi.data.test.AbstractTestDataImport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
public class MenuPermissionServiceTest extends AbstractTestDataImport {

    private static final Logger log = LoggerFactory.getLogger(MenuPermissionServiceTest.class);

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Before
    public void init() throws Exception {
        setDataSets("dataset/test-menu-permission.dataset.xml");
    }

    @After
    public void finish() throws Exception {
        log.debug("Test done, clearing data ...");
        clearDataSets();
    }

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
