package utils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

public class ExcelUtil {

    // We make this method 'static' so we can call it without creating an object!
    public static Object[][] getExcelData(String fileName, String sheetName) {

        Object[][] data = null;

        try {
            // 1. SENIOR PATHING: We dynamically find the project folder on ANY computer
            String projectPath = System.getProperty("user.dir");
            String filePath = projectPath + "/src/test/resources/testdata/" + fileName;

            // 2. OPEN THE NESTING DOLLS
            FileInputStream file = new FileInputStream(filePath); // Open the file
            XSSFWorkbook workbook = new XSSFWorkbook(file); // Open the Workbook
            XSSFSheet sheet = workbook.getSheet(sheetName); // Open the Sheet

            // 3. MEASURE THE GRID
            int rowCount = sheet.getPhysicalNumberOfRows(); // HOw many rows have data?
            int colCount = sheet.getRow(0).getLastCellNum(); // HOw many columns wide?

            // We create the empty 2D array. (rowCount - 1) because we skip the header!
            data = new Object[rowCount - 1][colCount];

            // 4. LOOP THROUGH THE CELLS
            // Start 'i' at 1 to skip the header row (Row 0)
            for (int i = 1; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    // Extract the text and put it in our Java Array
                    data[i - 1][j] = sheet.getRow(i).getCell(j).getStringCellValue();
                }
            }

            // 5. CLEAN UP: Close the file to prevent memory leaks!
            workbook.close();
            file.close();
        } catch (Exception e) {
            System.out.println("CRASH: Could not read the Excel file!");
            e.printStackTrace();
        }

        // 6. return the filled array back to TestNG
        return data;
    }
}
