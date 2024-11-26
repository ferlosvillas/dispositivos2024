package ar.edu.um.programacion2.trabajo_final.domain.DTO;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaOutDTO {

    protected Long idVenta;
    protected Long idDispositivo;
    protected String codigo;
    protected String nombre;
    protected String descripcion;
    protected BigDecimal precioBase;
    protected String moneda;
    protected List<CaracteristicaOutDTO> caracteristicas;
    protected List<PersonalizacionOutDTO> personalizaciones;
    protected List<AdicionalOutDTO> adicionales;
}
