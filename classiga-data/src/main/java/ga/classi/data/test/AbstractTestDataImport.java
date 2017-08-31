package ga.classi.data.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ga.classi.data.helper.DataImporter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
public abstract class AbstractTestDataImport {

    @Autowired
    protected DataImporter importer;

    private List<File> dataSets;

    /**
     * Process the dataset XML file(s).
     *
     * @param paths Dataset path relative to directory /src/test/resources
     * @throws java.lang.Exception
     */
    protected void setDataSets(String... paths) throws Exception {
        dataSets = new ArrayList<File>();
        for (String path : paths) {
            URL url = getClass().getClassLoader().getResource(path);
            File file = new File(url.getFile());
            dataSets.add(file);
            log.debug("Dataset file: " + file.getAbsolutePath());
        }
        try {
            importer.setFileDataSets(dataSets);
            importer.importAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    protected void clearDataSets() throws Exception {
        try {
            importer.setFileDataSets(dataSets);
            importer.deleteAll();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw e;
        }
    }

}
