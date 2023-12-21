package io.bootify.practica_spring_batch2.controller;

import io.bootify.practica_spring_batch2.service.CsvGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsvGeneratorController {

    @Autowired
    private CsvGeneratorService csvGeneratorService;

    @GetMapping("/generate-csv")
    public String generateCsv() {
        String csvFilePath = "transacciones.csv";
        int numberOfTransactions = 10000;
        csvGeneratorService.generateCsv(csvFilePath, numberOfTransactions);
        return "Archivo CSV generado con éxito.";
    }
}

