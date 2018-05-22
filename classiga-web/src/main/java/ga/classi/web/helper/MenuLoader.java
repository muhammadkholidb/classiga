package ga.classi.web.helper;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eatonmunoz
 */
public class MenuLoader {

    private static final Logger log = LoggerFactory.getLogger(MenuLoader.class);

    private JSONArray nestedMenus;
    private JSONArray flatMenus;

    private static final String SUFFIX_FILE_MENU = ".menu.json";
    
    @SuppressWarnings("unchecked")
    public void loadFrom(String dir) {

        File[] menuFiles = new File(dir).listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(SUFFIX_FILE_MENU);
            }
        });

        flatMenus = new JSONArray();
        nestedMenus = new JSONArray();
        JSONParser parser = new JSONParser();
        try {
            
            for (File file : menuFiles) {
                JSONArray array = (JSONArray) parser.parse(new FileReader(file));
                flatMenus.addAll(array);
            }
            
            String lastParentCode = null;
            
            for (Object object : flatMenus) {
                JSONObject menu = (JSONObject) object;
                String code = (String) menu.get(MenuKeyConstants.CODE);
                String parentCode = (String) menu.get(MenuKeyConstants.PARENT_CODE);
                JSONObject nestedMenu = (JSONObject) menu.clone();
                if (parentCode == null || parentCode.isEmpty()) {
                    nestedMenu.put(MenuKeyConstants.SUBMENUS, getChildren(flatMenus, code));
                    nestedMenus.add(nestedMenu);
                    lastParentCode = code;
                } else if (!parentCode.equals(lastParentCode)) {
                    nestedMenus.add(nestedMenu);
                }
            }
            
            sortBySequence(nestedMenus);
            
        } catch (IOException | ParseException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public JSONArray getChildren(JSONArray menus, String parentCode) {
        JSONArray children = new JSONArray();
        for (Object menu : menus) {
            JSONObject json = (JSONObject) menu;
            if (parentCode.equals(json.get(MenuKeyConstants.PARENT_CODE))) {
                children.add(json);
            }
        }
        sortBySequence(children); 
        return children;
    }

    @SuppressWarnings("unchecked")
    private void sortBySequence(JSONArray menus) {
        Collections.sort(menus, (JSONObject menu1, JSONObject menu2) -> {
            Object seq1 = menu1.get("sequence");
            Object seq2 = menu2.get("sequence");
            if (seq1 == null || seq1.toString().isEmpty()) {
                seq1 = 0;
            }
            if (seq2 == null || seq2.toString().isEmpty()) {
                seq2 = 0;
            }
            return Integer.valueOf(seq1.toString()).compareTo(Integer.valueOf(seq2.toString()));
        });
    }
    
    public JSONArray getNestedMenus() {
        return nestedMenus;
    }

    public JSONArray getFlatMenus() {
        return flatMenus;
    }

}
