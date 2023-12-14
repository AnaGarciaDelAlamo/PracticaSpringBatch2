package io.bootify.practica_spring_batch2.repos;

import io.bootify.practica_spring_batch2.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
