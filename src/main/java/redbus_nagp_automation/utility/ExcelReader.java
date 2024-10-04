package redbus_nagp_automation.utility;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class to read test data from an Excel file.
 */
public class ExcelReader {

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    /**
     * Method to read test data from an Excel file.
     * 
     * @param filePath  The path of the Excel file.
     * @param sheetName The name of the sheet containing the test data.
     * @return An array of test data read from the Excel file.
     */
    public static Object[][] readTestData(String filePath, String sheetName) {
        Object[][] testData = null;
        FileInputStream fileInputStream = null;
        Workbook workbook = null;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("Excel file does not exist at the specified location: " + filePath);
                return null;
            }

            fileInputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(fileInputStream);
            if (workbook == null) {
                log.error("Failed to create workbook from the Excel file: " + filePath);
                return null;
            }

            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getLastRowNum();
            int colCount = 3; // Assuming three columns: Source City, Destination City, and Buses Found

            // Initialize testData array excluding the header row
            testData = new Object[rowCount][colCount];

            // Start iterating from the second row (index 1)
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell sourceCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell destinationCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell busesFoundCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    
                    testData[i - 1][0] = sourceCell.toString();
                    testData[i - 1][1] = destinationCell.toString();
                    testData[i - 1][2] = busesFoundCell.toString();
                }
            }
        } catch (IOException e) {
            log.error("An error occurred while reading the Excel file: " + e.getMessage(), e);
        } finally {
            // Close resources in finally block to ensure they are always closed
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                log.error("Error while closing workbook or file input stream: " + ex.getMessage(), ex);
            }
        }
        return testData;
    }
}
