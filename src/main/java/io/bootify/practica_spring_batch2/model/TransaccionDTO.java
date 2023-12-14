package io.bootify.practica_spring_batch2.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransaccionDTO {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Double monto;

    @NotNull
    @Size(max = 255)
    private String tipoTransaccion;

    @NotNull
    @Size(max = 255)
    private String cuentaOrigen;

    @NotNull
    @Size(max = 255)
    private String cuentaDestino;

}
