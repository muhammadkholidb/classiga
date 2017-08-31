package ga.classi.data.service;

import ga.classi.data.service.SystemService;
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

import ga.classi.data.error.DataException;
import ga.classi.data.error.ExceptionCode;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.data.error.ErrorMessageConstants;
import ga.classi.data.helper.Dto;
import ga.classi.data.test.AbstractTestDataImport;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
public class SystemServiceTest extends AbstractTestDataImport {

    private static final Logger log = LoggerFactory.getLogger(SystemServiceTest.class);

    @Autowired
    private SystemService systemService;

    @Before
    public void init() throws Exception {
        setDataSets("dataset/test-system.dataset.xml");
    }

    @After
    public void finish() throws Exception {
        log.debug("Test done, clearing data ...");
        clearDataSets();
    }

    @Test
    public void testGetAllSystem() {
        log.debug("Test get all system ...");
        try {
            Dto result = systemService.getAllSystem(null);
            log.debug("Result: " + result);
            List<Dto> list = result.get(CommonConstants.CONTENT);
            assertEquals(4, list.size());
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            fail(ex.toString());
        }
    }

    @Test
    public void testGetSystemByKeySuccess() {
        log.debug("Test success get system by key ...");
        Dto dtoInput = new Dto();
        dtoInput.put("dataKey", CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        try {
            Dto result = systemService.getSystemByDataKey(dtoInput).get(CommonConstants.CONTENT);
            assertEquals("en", result.get("dataValue"));
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testGetSystemByKeyFail() {
        log.debug("Test fail get system by key ...");
        Dto dtoInput = new Dto();
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

        Dto dtoInput = new Dto().put("systems", systemList.toString());

        try {
            List<Dto> result = systemService.editSystemList(dtoInput).get(CommonConstants.CONTENT);
            log.debug("Result: " + result);
            assertEquals(2, result.size());
            for (Dto dto : result) {
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

        Dto dtoInput = new Dto().put("systems", systemList.toString());

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
