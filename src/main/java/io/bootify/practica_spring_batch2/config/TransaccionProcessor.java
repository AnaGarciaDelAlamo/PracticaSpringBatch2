package io.bootify.practica_spring_batch2.config;

import io.bootify.practica_spring_batch2.domain.Transaccion;
import org.springframework.batch.item.ItemProcessor;

public class TransaccionProcessor implements ItemProcessor<Transaccion, Transaccion> {
    @Override
    public Transaccion process(final Transaccion transaccion) throws Exception {
        return transaccion;
    }
}
