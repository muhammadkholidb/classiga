package ga.classi.data.helper;

import java.io.File;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = "classpath:classiga-data-context-test.xml")
public class DataImporterTest {

    @Autowired
    private DataImporter importer;
    
    @Test
    public void testImport() {
        log.debug("Test import ...");
        // Read: http://jlorenzen.blogspot.co.id/2007/06/proper-way-to-access-file-resources-in.html
        URL url1 = this.getClass().getResource("/dataset/test-importer-system.dataset.xml");
        URL url2 = this.getClass().getResource("/dataset/test-importer-user-group.dataset.xml");
        try {
            importer.addFileDataSet(new File(url1.getFile()));
            importer.addFileDataSet(new File(url2.getFile()));
            importer.importAll();
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

}
