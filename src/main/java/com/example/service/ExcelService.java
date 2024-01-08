package com.example.service;


import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ExcelService {


    public void writeToExcel(Map<Integer, Object[]> data, String sheetList, String filename) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetList);

        Set<Integer> keyset = data.keySet();

        int rownum = 0;
        for (Integer key : keyset) {

            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);

                if (obj instanceof String string) {
                    cell.setCellValue(string);
                } else if (obj instanceof Integer integer) {
                    cell.setCellValue(integer);
                } else if (obj instanceof Double doubleValue) {
                    cell.setCellValue(doubleValue);
                } else if (obj instanceof Float floatValue) {
                    cell.setCellValue(floatValue);
                } else if (obj instanceof Long longValue) {
                    cell.setCellValue(longValue);
                } else if (obj instanceof Boolean booleanValue) {
                    cell.setCellValue(booleanValue);
                } else if (obj instanceof Date date) {
                    cell.setCellValue(date);
                }

            }
        }

        try {
            log.info("Start try to write to file {}", filename);
            FileOutputStream out = new FileOutputStream(filename + ".xlsx");
            workbook.write(out);
            out.close();
            log.info("Successfully written to file {}", filename);
        } catch (Exception e) {
            log.error("Error: {} Message: {}", e.getClass(), e.getMessage());
        }
    }
}
