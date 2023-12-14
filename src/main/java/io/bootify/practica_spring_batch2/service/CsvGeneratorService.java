package io.bootify.practica_spring_batch2.service;

import io.bootify.practica_spring_batch2.domain.Transaccion;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

@Service
public class CsvGeneratorService {

    private static final String CSV_HEADER = "id,fecha,monto,tipoTransaccion,cuentaOrigen,cuentaDestino";

    public void generateCsv(String csvFilePath, int numberOfTransactions) {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            // Escribir el encabezado CSV
            writer.append(CSV_HEADER);
            writer.append("\n");

            // Generar transacciones aleatorias y escribirlas en el archivo CSV
            for (int i = 0; i < numberOfTransactions; i++) {
                Transaccion transaction = generateRandomTransaction();
                writer.append(String.valueOf(transaction.getId()));
                writer.append(",");
                writer.append(String.valueOf(transaction.getFecha()));
                writer.append(",");
                writer.append(String.valueOf(transaction.getMonto()));
                writer.append(",");
                writer.append(transaction.getTipoTransaccion());
                writer.append(",");
                writer.append(transaction.getCuentaOrigen());
                writer.append(",");
                writer.append(transaction.getCuentaDestino());
                writer.append("\n");
            }

            System.out.println("Archivo CSV generado con éxito.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Transaccion generateRandomTransaction() {
        Transaccion transaction = new Transaccion();
        // Configura los campos de la transacción con valores aleatorios
        transaction.setFecha(generateRandomLocalDate());  // Usar el nuevo método
        transaction.setMonto(generateRandomAmount());
        transaction.setTipoTransaccion(generateRandomTransactionType());
        transaction.setCuentaOrigen(generateRandomAccount());
        transaction.setCuentaDestino(generateRandomAccount());

        return transaction;
    }

    private LocalDate generateRandomLocalDate() {
        // Lógica para generar LocalDate aleatorias
        int year = 2022 + new Random().nextInt(3);  // Año entre 2022 y 2024
        int month = 1 + new Random().nextInt(12);   // Mes entre 1 y 12
        int day = 1 + new Random().nextInt(28);     // Día entre 1 y 28 (ajustar según necesidades)

        return LocalDate.of(year, month, day);
    }

    private double generateRandomAmount() {
        // Lógica para generar montos aleatorios
        return new Random().nextDouble() * 1000; // Ejemplo: Montos aleatorios entre 0 y 1000
    }

    private String generateRandomTransactionType() {
        // Lógica para generar tipos de transacción aleatorios
        String[] transactionTypes = {"Débito", "Crédito", "Transferencia"};
        return transactionTypes[new Random().nextInt(transactionTypes.length)];
    }

    private String generateRandomAccount() {
        // Lógica para generar cuentas aleatorias
        // Puedes personalizar según tus necesidades
        return "Cuenta" + new Random().nextInt(1000);
    }
}
