package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.Adicional;
import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import ar.edu.um.programacion2.trabajo_final.repository.DispositivoRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CambioValoresService {

    private static final Logger log = LoggerFactory.getLogger(CambioValoresService.class);

    @Autowired
    DispositivoRepository dispositivoRepository;

    @Autowired
    DispositivoService dispositivoService;

    @Autowired
    AdicionalService adicionalService;

    @Scheduled(fixedRate = 1800000)
    public boolean cambioAutomático() {
        log.info("Cambio programado cada 1 hora");
        BigDecimal precioInicial = new BigDecimal(450.0);
        this.cambiarPrecioDispositivo();
        this.cambiarPrecioAdicional();
        this.verCambios();
        return true;
    }

    //@Scheduled(fixedRate = 3000)
    public void change1() {
        this.cambiarPrecioAdicional();
        this.cambiarPrecioDispositivo();
        this.verCambios();
    }

    //@Scheduled(fixedRate = 3000)
    public void cambiarPrecioDispositivo() {
        log.info("Cambio valores dispositivo");
        BigDecimal precioInicial = new BigDecimal(450.0);
        Double variacion = (Math.random() * 10) - 5;
        BigDecimal precioCambiado = precioInicial.add(BigDecimal.valueOf((Math.floor(variacion * 100) / 100)));
        Optional<Dispositivo> dispositivoO = this.dispositivoService.findOne(1L);
        if (dispositivoO.isPresent()) {
            log.info("Dispositivo encontrado");
            Dispositivo dispositivo = dispositivoO.get();
            //log.info(dispositivoO.get().getPrecioBase().toString());
            //log.info("Nuevo valor: "+precioCambiado.toString());
            dispositivo.setPrecioBase(precioCambiado);
            dispositivoRepository.save(dispositivo);
        }
    }

    public void cambiarPrecioAdicional() {
        log.info("Cambio valores adicional");
        BigDecimal precioInicial = new BigDecimal(78.0);
        Double variacion = (Math.random() * 10) - 5;
        BigDecimal precioCambiado = precioInicial.add(BigDecimal.valueOf((Math.floor(variacion * 100) / 100)));
        Optional<Adicional> adicionalO = this.adicionalService.findOne(2L);
        if (adicionalO.isPresent()) {
            log.info("Adicional encontrado");
            Adicional adicional = adicionalO.get();
            log.info("Precion actual: " + adicional.getPrecio());
            log.info("Nuevo precio: " + precioCambiado);
            adicional.setPrecio(precioCambiado);
            this.adicionalService.save(adicional);
        }
    }

    public void verCambios() {
        log.info("Mostrar valores");
        Optional<Dispositivo> dispositivoO = this.dispositivoService.findOne(1L);
        if (dispositivoO.isPresent()) {
            log.info("Dispositivo encontrado");
            log.info(dispositivoO.get().getPrecioBase().toString());
        }

        Optional<Adicional> adicionalO = this.adicionalService.findOne(2L);
        if (adicionalO.isPresent()) {
            log.info("Adicional encontrado");
            log.info(adicionalO.get().getPrecio().toString());
        }
    }
}
