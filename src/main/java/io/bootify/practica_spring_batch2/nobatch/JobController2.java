/*package io.bootify.practica_spring_batch2.nobatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController2 {

    @Autowired
    private CsvToDatabaseImporterService importerService;

    @PostMapping("/importTransacciones")
    public ResponseEntity<String> importCsvToDBJob() {
        try {
            importerService.importarCsv("src/main/resources/transacciones.csv");
            return ResponseEntity.ok("Job completed successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Manejar la excepción de manera adecuada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Job execution failed");
        }
    }
}*/

