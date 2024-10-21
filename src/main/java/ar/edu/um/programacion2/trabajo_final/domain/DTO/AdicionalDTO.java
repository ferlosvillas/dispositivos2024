package ar.edu.um.programacion2.trabajo_final.domain.DTO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdicionalDTO {

    protected Long id;
    protected BigDecimal precio;
}
