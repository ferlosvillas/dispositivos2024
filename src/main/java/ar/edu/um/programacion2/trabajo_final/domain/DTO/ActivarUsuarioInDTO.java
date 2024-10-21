package ar.edu.um.programacion2.trabajo_final.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivarUsuarioInDTO {

    protected String login;
    protected String email;
    protected String nombres;
    protected String descripcion;
}
