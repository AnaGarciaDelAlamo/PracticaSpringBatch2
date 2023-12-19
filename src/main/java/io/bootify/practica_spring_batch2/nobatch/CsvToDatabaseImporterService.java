/*package io.bootify.practica_spring_batch2.nobatch;

import io.bootify.practica_spring_batch2.domain.Transaccion;
import io.bootify.practica_spring_batch2.repos.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class CsvToDatabaseImporterService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Transactional
    public void importarCsv(String rutaCsv) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCsv))) {
            String linea;
            br.readLine();

            while ((linea = br.readLine()) != null) {
                procesarLinea(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarLinea(String linea) {

        String[] valores = linea.split(",");

        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(LocalDate.parse(valores[1]));
        transaccion.setMonto(Double.parseDouble(valores[2]));
        transaccion.setTipoTransaccion(valores[3]);
        transaccion.setCuentaOrigen(valores[4]);
        transaccion.setCuentaDestino(valores[5]);
        transaccion.setDateCreated(OffsetDateTime.now());
        transaccion.setLastUpdated(OffsetDateTime.now());

        transaccionRepository.save(transaccion);
    }
}*/
