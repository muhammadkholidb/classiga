package ga.classi.data.test;

import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.springframework.core.io.Resource;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
@DbUnitConfiguration(dataSetLoader = DefaultSpringTestDbUnitConfiguration.ReplacementFlatXmlDataSetLoader.class, databaseConnection = "dataSource")
public class DefaultSpringTestDbUnitConfiguration {
    
    public static class ReplacementFlatXmlDataSetLoader extends FlatXmlDataSetLoader {

        @Override
        public IDataSet loadDataSet(Class<?> testClass, String location) {
            try {
                return super.loadDataSet(testClass, location);
            } catch (Exception e) {
                log.error("Failed to load data set.", e);
            }
            return null;
        }

        @Override
        protected IDataSet createDataSet(Resource resource) throws Exception {
            // The replacements will set the same values per dataset
            ReplacementDataSet dataSet = new ReplacementDataSet(super.createDataSet(resource));
            dataSet.addReplacementObject("{currentDate}", new Date());
            dataSet.addReplacementObject("{currentMillis}", System.currentTimeMillis());
            dataSet.addReplacementObject("{null}", null);
            return dataSet; 
        }
        
    }
    
}
