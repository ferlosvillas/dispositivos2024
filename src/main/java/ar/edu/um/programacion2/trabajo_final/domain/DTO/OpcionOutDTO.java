package ar.edu.um.programacion2.trabajo_final.domain.DTO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionOutDTO {

    protected Long id;
    protected String codigo;
    protected String nombre;
    protected String descripcion;
    protected BigDecimal precioAdicional;
}
