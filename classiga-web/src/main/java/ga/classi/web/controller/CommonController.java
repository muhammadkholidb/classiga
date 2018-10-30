/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.web.controller;

import ga.classi.commons.web.utility.JSON;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author muhammad
 */
@Controller
public class CommonController {
    
    public String favicon() {
        return null;
    }
    
    public String robot() {
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @PostMapping("/generate-html")
    public String generateHtml(
            @RequestParam(name = "template") String encodedTemplate, 
            @RequestParam(name = "data") String encodedData, 
            ModelMap model) throws UnsupportedEncodingException {
        
        String decodedTemplate = URLDecoder.decode(encodedTemplate, "UTF-8");
        String decodedData = URLDecoder.decode(encodedData, "UTF-8");
        model.addAllAttributes(JSON.parse(decodedData, Map.class));
        return decodedTemplate;
    }
    
}
