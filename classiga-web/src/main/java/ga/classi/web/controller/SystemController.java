/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ga.classi.commons.utility.ActionResult;
import ga.classi.commons.constant.CommonConstants;
import ga.classi.commons.constant.StringConstants;
import ga.classi.web.controller.base.BaseControllerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
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
            String lang = locale.getLanguage();
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", new Locale("id").getLanguage().equals(lang) ? "id" : lang);
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
    
    @GetMapping(value = "/settings/system/edit")
    public ModelAndView edit() {
        log.info("Edit system ...");
        return view("system/form", prepareForm(true));
    }
    
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/settings/system/edit")
    public ModelAndView edit(
            @RequestParam(name = "languageCode", required = true, defaultValue = StringConstants.EMPTY) String languageCode, 
            @RequestParam(name = "online", required = true, defaultValue = StringConstants.EMPTY) String online) {

        log.info("Submit edit system ...");
        
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
        }
        
        // Fail or error
        return redirectAndNotifyError("/settings/system", result.getMessage());        
    }
    
}
