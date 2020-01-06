package com.example.ExcelToJsonConverter.services;

import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ExcelReader {
    private static final String FILE_NAME = "src/main/resources/example.xlsx";

    private static Logger logger = Logger.getLogger("MyLog");
    FileHandler fh;


    public void reader() {

        try {
            Date time = new Date();
            fh = new FileHandler("src/main/resources/excelReaderLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);


            JsonObject message = new JsonObject();

            JsonObject data = new JsonObject();

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int row = 0;
            List<String> headers = new ArrayList<>();
            String dataKey="";
            String dataValue="";
            Boolean isDataValue = false;
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();

                Iterator<Cell> cellIterator = currentRow.iterator();
                int cellIndex = 0;
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (row == 0){

                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            headers.add(currentCell.getStringCellValue());
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            headers.add((String.valueOf(currentCell.getNumericCellValue())));
                        }
                    }

                    if (!headers.get(cellIndex).equals("data")){
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            message.addProperty(headers.get(cellIndex),currentCell.getStringCellValue());
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            message.addProperty(headers.get(cellIndex),currentCell.getNumericCellValue());
                        }
                    }else{
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            dataKey = currentCell.getStringCellValue();
                            //message.addProperty(headers.get(cellIndex),currentCell.getStringCellValue());
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            dataKey = (String.valueOf(currentCell.getNumericCellValue()));
                            //message.addProperty(headers.get(cellIndex),(String.valueOf(currentCell.getNumericCellValue())));
                        }
                        isDataValue = true;
                    }
                    if (!headers.get(cellIndex).equals("data") && isDataValue){
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                            //dataKey = currentCell.getStringCellValue();
                            data.addProperty(dataKey,currentCell.getStringCellValue());
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            //dataKey = (String.valueOf(currentCell.getNumericCellValue()));
                            data.addProperty(dataKey,(String.valueOf(currentCell.getNumericCellValue())));
                        }
                        isDataValue=false;
                    }

                    cellIndex++;

                }row++;

                System.out.println(message.toString());
                logger.info("Enviando Mensaje a EventGenerator -->Row #"+ row + "---> Milis time:--->"+time.getTime()+"--> Contenido-->:" + message.toString() );
                RabbitPublisher.rabbitPulblisher(message.toString());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
