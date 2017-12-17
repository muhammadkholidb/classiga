package ga.classi.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.helper.ActionResult;
import ga.classi.commons.helper.CommonConstants;
import ga.classi.commons.helper.StringConstants;
import ga.classi.web.controller.base.BaseControllerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
@Controller
public class SystemController extends BaseControllerAdapter {

    @GetMapping(value = "/settings/system")
    public ModelAndView index() {
        log.info("Index ...");
        return view("system/form", prepareForm(false));
    }
    
    private Map<String, Object> prepareForm(boolean isEdit) {
        log.info("Prepare form ...");
        String currentLanguageCode = getSystem(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        String currentTemplateCode = getSystem(CommonConstants.SYSTEM_KEY_TEMPLATE_CODE);
        String currentOnline = getSystem(CommonConstants.SYSTEM_KEY_ONLINE);
        
        List<Map<String, String>> languages = new ArrayList<Map<String,String>>();
        List<Locale> locales = getSupportedLocales("label.language");
        for (Locale locale : locales) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", locale.getLanguage());
            map.put("name", locale.getDisplayLanguage(new Locale(currentLanguageCode)));
            languages.add(map);
        }
        
        List<Map<String, String>> templates = new ArrayList<Map<String,String>>();
        List<String> templateCodes = getAvailableTemplates();
        for (String code : templateCodes) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", code);
            map.put("name", WordUtils.capitalize(code));
            templates.add(map);
        }
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("languages", languages);
        model.put("templates", templates);
        model.put("currentLanguageCode", currentLanguageCode);
        model.put("currentTemplateCode", currentTemplateCode);
        model.put("currentOnline", currentOnline);
        model.put("isEdit", isEdit);
        
        return model;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settings/system/edit", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView edit(
            @RequestParam(name = "languageCode", required = true, defaultValue = StringConstants.EMPTY) String languageCode, 
            @RequestParam(name = "online", required = true, defaultValue = StringConstants.EMPTY) String online) throws IOException {

        log.info("Edit ...");
        
        if (isPost()) {
            
            JSONArray editSystems = new JSONArray();

            JSONArray systems = getSystems();
            for (Object object : systems) {
                JSONObject system = (JSONObject) object;
                if (CommonConstants.SYSTEM_KEY_LANGUAGE_CODE.equals(system.get("dataKey"))) {
                    JSONObject data = new JSONObject();
                    data.put("id", system.get("id"));
                    data.put("dataValue", languageCode);
                    editSystems.add(data);
                } else if (CommonConstants.SYSTEM_KEY_ONLINE.equals(system.get("dataKey"))) {
                    JSONObject data = new JSONObject();
                    data.put("id", system.get("id"));
                    data.put("dataValue", online);
                    editSystems.add(data);
                }
            }

            JSONObject params = new JSONObject();
            params.put("systems", editSystems.toJSONString());

            ActionResult result = editSystems(params, languageCode);
            
            if (CommonConstants.SUCCESS.equals(result.getStatus())) {
                loadSystems();
                return redirectAndNotifySuccess("/settings/system", result.getMessage());
            } else {    // Fail or error
                return redirectAndNotifyError("/settings/system", result.getMessage());
            }        
        }
        
        return view("system/form", prepareForm(true));
    }
    
}
