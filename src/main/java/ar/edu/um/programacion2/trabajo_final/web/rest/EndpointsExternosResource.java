package ar.edu.um.programacion2.trabajo_final.web.rest;

import ar.edu.um.programacion2.trabajo_final.domain.DTO.CrearVentaDTO;
import ar.edu.um.programacion2.trabajo_final.domain.DTO.VentaListaOutDTO;
import ar.edu.um.programacion2.trabajo_final.domain.DTO.VentaOutDTO;
import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import ar.edu.um.programacion2.trabajo_final.service.CambioValoresService;
import ar.edu.um.programacion2.trabajo_final.service.DispositivoService;
import ar.edu.um.programacion2.trabajo_final.service.ManejadorVentaService;
import ar.edu.um.programacion2.trabajo_final.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catedra")
public class EndpointsExternosResource {

    private static final Logger log = LoggerFactory.getLogger(DispositivoResource.class);

    private final DispositivoService dispositivoService;

    private final ManejadorVentaService manejadorVentaService;

    private final CambioValoresService cambioValoresService;

    public EndpointsExternosResource(
        DispositivoService dispositivoService,
        ManejadorVentaService manejadorVentaService,
        CambioValoresService cambioValoresService
    ) {
        this.dispositivoService = dispositivoService;
        this.manejadorVentaService = manejadorVentaService;
        this.cambioValoresService = cambioValoresService;
    }

    // Get dispositivos
    @GetMapping("/dispositivos")
    public List<Dispositivo> getAllDispositivos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.info("REST request to get all Dispositivos");
        List<Dispositivo> result = dispositivoService.findAll();
        return result;
    }

    @PostMapping("/vender")
    public ResponseEntity<VentaOutDTO> crearVenta(@RequestBody CrearVentaDTO crearVentaDTO, Principal principal) throws URISyntaxException {
        log.info("REST request to crear Venta : {}", crearVentaDTO);
        Venta venta = this.manejadorVentaService.guardarVenta(crearVentaDTO, principal);
        if (venta == null) {
            throw new BadRequestAlertException("Datos invalidos", null, "");
        }
        VentaOutDTO ventaOutDTO = this.manejadorVentaService.getVentaOutDTODesdeVenta(venta);
        if (ventaOutDTO == null) {
            throw new BadRequestAlertException("Datos invalidos - 2", null, "");
        }

        return ResponseEntity.ok(ventaOutDTO);
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<VentaListaOutDTO>> listarVentas(Principal principal) {
        log.info("Listar ventas de " + principal.getName().toString());
        List<VentaListaOutDTO> ventas = this.manejadorVentaService.getVentasPorGrupo(principal);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/venta/{id}")
    public ResponseEntity<VentaOutDTO> getVenta(@PathVariable Long id, Principal principal) {
        log.info("Listar venta id: " + id.toString());
        VentaOutDTO venta = this.manejadorVentaService.getVentaOutById(principal, id);
        return ResponseEntity.ok(venta);
    }

    @GetMapping("/cambio_d1")
    public void cambioD1() {
        log.info("Endpoint de cambio de valor de dispositivo");
        this.cambioValoresService.cambiarPrecioDispositivo();
        this.cambioValoresService.verCambios();
    }

    @GetMapping("/cambio_d2")
    public void cambioD2() {
        log.info("Endpoint de cambio de valor de adicional");
        this.cambioValoresService.cambiarPrecioAdicional();
        this.cambioValoresService.verCambios();
    }
}
