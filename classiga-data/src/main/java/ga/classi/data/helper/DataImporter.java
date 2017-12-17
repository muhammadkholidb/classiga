package ga.classi.data.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ga.classi.commons.data.error.DataException;
import ga.classi.commons.data.error.ExceptionCode;
import ga.classi.commons.helper.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author eatonmunoz
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
    private DataSource dataSource;

    private String sqlSequenceName;

    private String sqlResetSequence;

    private List<File> fileDataSets;

    private List<InputStream> streamDataSets;

    public void importAll() throws DataException {
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
                IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
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
            String message = CommonUtils.getExceptionMessage(ex);
            log.error(message, ex);
            throw new DataException(ExceptionCode.E1007, message);
        }
    }

    public void addFileDataSet(File dataSet) {
        if (this.fileDataSets == null) {
            this.fileDataSets = new ArrayList<File>();
        }
        this.fileDataSets.add(dataSet);
    }

    public void addStreamDataSet(InputStream inputStreamDataSet) {
        if (this.streamDataSets == null) {
            this.streamDataSets = new ArrayList<InputStream>();
        }
        this.streamDataSets.add(inputStreamDataSet);
    }

    public void deleteAll() throws DataException {
        try {
            if ((fileDataSets != null) && !fileDataSets.isEmpty()) {
                for (File file : fileDataSets) {
                    addStreamDataSet(new FileInputStream(file));
                }
                fileDataSets = null;
            }
            if ((streamDataSets != null) && !streamDataSets.isEmpty()) {
                IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
                for (InputStream is : streamDataSets) {
                    IDataSet ids = new FlatXmlDataSetBuilder().build(is);
                    DatabaseOperation.DELETE_ALL.execute(connection, ids);
                }
                streamDataSets = null;
            }
        } catch (Exception ex) {
            String message = CommonUtils.getExceptionMessage(ex);
            log.error(message, ex);
            throw new DataException(ExceptionCode.E1007, message);
        }
    }

    private class CleanInsertOperation extends DatabaseOperation {

        private final String REPLACEMENT_KEY_DATE = "currentDate";
        private final String REPLACEMENT_KEY_MILLISECOND = "currentMillis";

        @Override
        public void execute(IDatabaseConnection idc, IDataSet ids) throws DatabaseUnitException, SQLException {

            ReplacementDataSet rds = new ReplacementDataSet(ids);

            rds.addReplacementObject("{" + REPLACEMENT_KEY_DATE + "}", new Date());
            rds.addReplacementObject("{" + REPLACEMENT_KEY_MILLISECOND + "}", System.currentTimeMillis());

            if (doResetSequence) {
                // Reset sequences
                Statement statement = idc.getConnection().createStatement();
                String[] tables = ids.getTableNames();
                for (String table : tables) {
                    try {
                        String query;
                        if ((sqlResetSequence != null) && !sqlResetSequence.isEmpty()) {
                            query = sqlResetSequence;
                        } else if ((sqlSequenceName != null) && !sqlSequenceName.isEmpty()) {
                            query = String.format(sqlResetSequence, sqlSequenceName);
                        } else {
                            query = String.format(sqlResetSequence, (sqlSequencePrefix + table + sqlSequencePostfix));
                        }
                        statement.execute(query);
                    } catch (Exception e) {
                        log.error(e.toString(), e);
                    }
                }
            }

            DatabaseOperation.CLEAN_INSERT.execute(idc, rds);
        }

    }

}
