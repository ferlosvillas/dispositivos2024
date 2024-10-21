package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.DTO.ActivarUsuarioInDTO;
import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.excepciones.ActivarUsuarioException;
import java.security.Principal;

public interface ManejoSesionService {
    public boolean activarUsuario(ActivarUsuarioInDTO usuarioDTO, Principal principal) throws ActivarUsuarioException;

    public boolean revisarDatosUsuario(ActivarUsuarioInDTO usuarioDTO);

    public Grupo obtenerGrupoDePrincipal(Principal principal);
}
