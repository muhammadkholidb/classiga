package ga.classi.data.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.data.helper.DTO;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.test.ReplacementFlatXmlDataSetLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
@TestExecutionListeners({ 
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("SystemServiceTest.xml")
@DatabaseTearDown("SystemServiceTestCleanup.xml")
@DbUnitConfiguration(dataSetLoader = ReplacementFlatXmlDataSetLoader.class, databaseConnection = "dbUnitDatabaseConnection")
public class SystemServiceTest {

    @Autowired
    private SystemService systemService;

    @Test
    public void testGetAllSystem() {
        log.debug("Test get all system ...");
        try {
            DTO result = systemService.getAllSystem(null);
            log.debug("Result: " + result);
            List<DTO> list = result.get(CommonConstants.CONTENT);
            assertEquals(4, list.size());
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            fail(ex.toString());
        }
    }

    @Test
    public void testGetSystemByKeySuccess() {
        log.debug("Test success get system by key ...");
        DTO dtoInput = new DTO();
        dtoInput.put("dataKey", CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        try {
            DTO result = systemService.getSystemByDataKey(dtoInput).get(CommonConstants.CONTENT);
            assertEquals("en", result.get("dataValue"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testGetSystemByKeyFail() {
        log.debug("Test fail get system by key ...");
        DTO dtoInput = new DTO();
        dtoInput.put("dataKey", "unknown");
        try {
            systemService.getSystemByDataKey(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.SYSTEM_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditSystemListSuccess() {
        log.debug("Test success edit system list ...");

        JSONObject systemLanguageCode = new JSONObject();
        systemLanguageCode.put("id", 1L);
        systemLanguageCode.put("dataKey", CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        systemLanguageCode.put("dataValue", CommonConstants.LANGUAGE_CODE_INDONESIA);

        JSONObject systemOnline = new JSONObject();
        systemOnline.put("id", 4L);
        systemOnline.put("dataKey", CommonConstants.SYSTEM_KEY_ONLINE);
        systemOnline.put("dataValue", CommonConstants.YES);

        JSONArray systemList = new JSONArray();
        systemList.add(systemLanguageCode);
        systemList.add(systemOnline);

        DTO dtoInput = new DTO().put("systems", systemList.toString());

        try {
            List<DTO> result = systemService.editSystemList(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: " + result);
            assertEquals(2, result.size());
            for (DTO dto : result) {
                Long id = dto.get("id");
                if (id == 1L) {
                    assertEquals(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE, dto.get("dataKey"));
                    assertEquals(CommonConstants.LANGUAGE_CODE_INDONESIA, dto.get("dataValue"));
                } else if (id == 4L) {
                    assertEquals(CommonConstants.SYSTEM_KEY_ONLINE, dto.get("dataKey"));
                    assertEquals(CommonConstants.YES, dto.get("dataValue"));
                }
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditSystemListFail() {
        log.debug("Test fail edit system list ...");

        JSONObject systemUnknown = new JSONObject();
        systemUnknown.put("id", 10L);
        systemUnknown.put("dataKey", "unknown");
        systemUnknown.put("dataValue", 0);

        JSONArray systemList = new JSONArray();
        systemList.add(systemUnknown);

        DTO dtoInput = new DTO().put("systems", systemList.toString());

        try {
            systemService.editSystemList(dtoInput);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.SYSTEM_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

}
