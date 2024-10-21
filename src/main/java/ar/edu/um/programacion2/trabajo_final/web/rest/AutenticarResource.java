package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.DTO.ActivarUsuarioInDTO;
import ar.edu.um.programacion2.trabajo_final.excepciones.ActivarUsuarioException;
import ar.edu.um.programacion2.trabajo_final.service.ManejoSesionService;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AutenticarResource {

    //@Autowired
    //protected ManejoSesionService manejoSesionService;

    @Autowired
    private ManejoSesionService manejoSesionService;

    private final Logger log = LoggerFactory.getLogger(AutenticarResource.class);

    @PostMapping("/activar")
    public void activar(@RequestBody ActivarUsuarioInDTO usuarioDTO, Principal principal) {
        if (this.manejoSesionService.revisarDatosUsuario(usuarioDTO)) {
            boolean activado;
            try {
                activado = this.manejoSesionService.activarUsuario(usuarioDTO, principal);
            } catch (ActivarUsuarioException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos incorrectos.");
        }
    }
}
