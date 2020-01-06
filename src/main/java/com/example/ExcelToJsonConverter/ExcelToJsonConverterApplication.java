package com.example.ExcelToJsonConverter;

import com.example.ExcelToJsonConverter.services.ExcelReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExcelToJsonConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelToJsonConverterApplication.class, args);
		ExcelReader excelReader = new ExcelReader();
		excelReader.reader();
	}

}
