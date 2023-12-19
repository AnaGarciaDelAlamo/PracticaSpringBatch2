package io.bootify.practica_spring_batch2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "transaccions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Data
public class Transaccion {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )

    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name= "monto",nullable = false)
    private Double monto;

    @Column(name = "tipoTransaccion",nullable = false)
    private String tipoTransaccion;

    @Column(name = "cuentaOrigen", nullable = false)
    private String cuentaOrigen;

    @Column(name = "cuentaDestino", nullable = false)
    private String cuentaDestino;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
