package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import ar.edu.um.programacion2.trabajo_final.service.DispositivoService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catedra")
public class EndpointsExternosResource {

    private static final Logger log = LoggerFactory.getLogger(DispositivoResource.class);

    private final DispositivoService dispositivoService;

    public EndpointsExternosResource(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    // Get dispositivos
    @GetMapping("/dispositivos")
    public List<Dispositivo> getAllDispositivos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Dispositivos");
        List<Dispositivo> result = dispositivoService.findAll();
        return result;
    }
}
