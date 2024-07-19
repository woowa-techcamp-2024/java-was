package codesquad.webserver.db.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CsvDatabaseInitializer.class);
    private static final String CSV_DIR_NAME = "csv_db";

    public static String initializeDatabase(String fileName, String header) throws IOException {
        String baseDir = System.getProperty("user.dir");
        String csvDirPath = baseDir + File.separator + CSV_DIR_NAME;
        File csvDir = new File(csvDirPath);

        if (!csvDir.exists()) {
            if (!csvDir.mkdirs()) {
                logger.error("Failed to create CSV directory: {}", csvDirPath);
                throw new IOException("Failed to create CSV directory: " + csvDirPath);
            }
        }

        String filePath = csvDirPath + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(header + "\n");
                logger.info("Created database file: {}", filePath);
            }
        }
        logger.info("Using CSV file: {}", filePath);
        return filePath;
    }

    public static String getJdbcUrl(String filePath) {
        return "jdbc:csv:" + filePath;
    }
}