/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.data.helper;

import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.Errors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
@Setter
@Getter
@Component
public class DataImporter {

    @Value("${importer.sql.ResetSequencePreformatted}")
    private String sqlResetSequencePreformatted;

    @Value("${importer.sql.SequencePostfix}")
    private String sqlSequencePostfix;

    @Value("${importer.sql.SequencePrefix}")
    private String sqlSequencePrefix;

    @Value("${importer.sql.DoResetSequence}")
    private Boolean doResetSequence;

    @Autowired
    private DatabaseDataSourceConnectionFactoryBean dataSourceConnectionFactory;
    
    private String sqlSequenceName;

    private String sqlResetSequence;

    private List<File> fileDataSets;

    private List<InputStream> streamDataSets;

    public void importAll() {
        log.debug("Import dataset ...");
        try {
            if ((fileDataSets != null) && !fileDataSets.isEmpty()) {
                for (File file : fileDataSets) {
                    addStreamDataSet(new FileInputStream(file));
                }
                fileDataSets = null;
            }
            if ((streamDataSets != null) && !streamDataSets.isEmpty()) {
                log.debug(streamDataSets.size() + " dataset(s) found");
                IDatabaseConnection connection = dataSourceConnectionFactory.getObject();
                CleanInsertOperation operation = new CleanInsertOperation();
                for (int i = 0; i < streamDataSets.size(); i++) {
                    log.debug("Processing " + (i + 1));
                    IDataSet ids = new FlatXmlDataSetBuilder().build(streamDataSets.get(i));
                    operation.execute(connection, ids);
                }
                streamDataSets = null;
            }
            log.debug("Import done.");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new DataException(Errors.UNKNOWN);
        }
    }

    public void addFileDataSet(File dataSet) {
        if (this.fileDataSets == null) {
            this.fileDataSets = new ArrayList<>();
        }
        this.fileDataSets.add(dataSet);
    }

    public void addStreamDataSet(InputStream inputStreamDataSet) {
        if (this.streamDataSets == null) {
            this.streamDataSets = new ArrayList<>();
        }
        this.streamDataSets.add(inputStreamDataSet);
    }

    public void deleteAll() {
        try {
            if ((fileDataSets != null) && !fileDataSets.isEmpty()) {
                for (File file : fileDataSets) {
                    addStreamDataSet(new FileInputStream(file));
                }
                fileDataSets = null;
            }
            if ((streamDataSets != null) && !streamDataSets.isEmpty()) {
                IDatabaseConnection connection = dataSourceConnectionFactory.getObject();
                for (InputStream is : streamDataSets) {
                    IDataSet ids = new FlatXmlDataSetBuilder().build(is);
                    DatabaseOperation.DELETE_ALL.execute(connection, ids);
                }
                streamDataSets = null;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new DataException(Errors.UNKNOWN);
        }
    }

    private class CleanInsertOperation extends DatabaseOperation {

        @Override
        public void execute(IDatabaseConnection idc, IDataSet ids) throws DatabaseUnitException, SQLException {

            // The replacements will set the same values per dataset
            ReplacementDataSet dataSet = new ReplacementDataSet(ids);
            dataSet.addReplacementObject("{currentDate}", new Date());
            dataSet.addReplacementObject("{currentMillis}", System.currentTimeMillis());
            dataSet.addReplacementObject("{null}", null);

            if (doResetSequence) {
                // Reset sequences
                Statement statement = idc.getConnection().createStatement();
                try {
                    String[] tables = ids.getTableNames();
                    for (String table : tables) {
                        String query;
                        if ((sqlResetSequence != null) && !sqlResetSequence.isEmpty()) {
                            query = sqlResetSequence;
                        } else if ((sqlSequenceName != null) && !sqlSequenceName.isEmpty()) {
                            query = String.format(sqlResetSequence, sqlSequenceName);
                        } else {
                            query = String.format(sqlResetSequence, (sqlSequencePrefix + table + sqlSequencePostfix));
                        }
                        statement.execute(query);
                    }
                } catch (Exception e) {
                    log.error(e.toString(), e);
                } finally {
                    statement.close();
                }
            }

            DatabaseOperation.CLEAN_INSERT.execute(idc, dataSet);
        }

    }

}
