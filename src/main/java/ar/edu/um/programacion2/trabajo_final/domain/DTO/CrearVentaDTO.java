package ar.edu.um.programacion2.trabajo_final.domain.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearVentaDTO {

    protected Long idDispositivo;
    protected List<PersonalizacionDTO> personalizaciones;
    protected List<AdicionalDTO> adicionales;
    protected BigDecimal precioFinal;
    protected Instant fechaVenta;
}
