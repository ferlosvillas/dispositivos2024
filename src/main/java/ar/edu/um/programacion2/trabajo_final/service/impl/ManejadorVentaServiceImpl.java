package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.*;
import ar.edu.um.programacion2.trabajo_final.domain.DTO.*;
import ar.edu.um.programacion2.trabajo_final.repository.VentaPersonalizadoRepository;
import ar.edu.um.programacion2.trabajo_final.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManejadorVentaServiceImpl implements ManejadorVentaService {

    @Autowired
    ManejoSesionService manejoSesionService;

    @Autowired
    DispositivoService dispositivoService;

    @Autowired
    VentaService ventaService;

    @Autowired
    AdicionalService adicionalService;

    @Autowired
    CaracteristicaService caracteristicaService;

    @Autowired
    PersonalizacionService personalizacionService;

    @Autowired
    OpcionService opcionService;

    @Autowired
    VentaPersonalizadoRepository ventaPersonalizadoRepository;

    private static final Logger log = LoggerFactory.getLogger(ManejadorVentaServiceImpl.class);

    @Override
    public Venta guardarVenta(CrearVentaDTO ventaDTO, Principal principal) {
        Grupo grupo = this.manejoSesionService.obtenerGrupoDePrincipal(principal);
        if (grupo == null) {
            return null;
        }
        Venta venta = new Venta();
        //IdVenta es el Id interno, lo tengo que setear despues de guardar la venta

        Optional<Dispositivo> dispositivoO = this.dispositivoService.findOne(ventaDTO.getIdDispositivo());
        if (dispositivoO.isEmpty()) {
            return null;
        }
        Dispositivo dispositivo = dispositivoO.get();
        venta.setCodigo(dispositivo.getCodigo());
        venta.setNombre(dispositivo.getNombre());
        venta.setDescripcion(dispositivo.getDescripcion());
        venta.setPrecio(ventaDTO.getPrecioFinal());
        venta.setGrupo(grupo);

        String ventaJson;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            ventaJson = objectMapper.writeValueAsString(ventaDTO);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
        venta.setVentaPedidoJson(ventaJson);

        Venta ventaGrabada = this.ventaService.save(venta);
        ventaGrabada.setIdVenta(venta.getId());
        VentaOutDTO ventaOutDTO = this.crearVentaDTOaVentaOutDTO(venta, ventaDTO);
        String ventaOutJson;
        try {
            ventaOutJson = objectMapper.writeValueAsString(ventaOutDTO);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
        ventaGrabada.setVentaResultadoJson(ventaOutJson);
        ventaGrabada = this.ventaService.save(ventaGrabada);
        return ventaGrabada;
    }

    protected VentaOutDTO crearVentaDTOaVentaOutDTO(Venta venta, CrearVentaDTO ventaDTO) {
        VentaOutDTO ventaOutDTO = new VentaOutDTO();
        ventaOutDTO.setIdVenta(venta.getId());
        ventaOutDTO.setIdDispositivo(ventaDTO.getIdDispositivo());
        ventaOutDTO.setCodigo(venta.getCodigo());
        ventaOutDTO.setNombre(venta.getNombre());
        ventaOutDTO.setDescripcion(venta.getDescripcion());
        ventaOutDTO.setPrecioBase(venta.getPrecio());
        Optional<Dispositivo> dispositivoO = this.dispositivoService.findOne(ventaDTO.getIdDispositivo());
        if (dispositivoO.isEmpty()) {
            return null;
        }
        Dispositivo dispositivo = dispositivoO.get();
        ventaOutDTO.setMoneda(dispositivo.getMoneda());

        List<AdicionalOutDTO> adicionales = new ArrayList<>();
        for (AdicionalDTO adicional : ventaDTO.getAdicionales()) {
            Optional<Adicional> adicionalO = this.adicionalService.findOne(adicional.getId());
            if (adicionalO.isPresent()) {
                Adicional adicionalDB = adicionalO.get();
                AdicionalOutDTO adicionalOutDTO = new AdicionalOutDTO();
                adicionalOutDTO.setId(adicionalDB.getId());
                adicionalOutDTO.setNombre(adicionalDB.getNombre());
                adicionalOutDTO.setDescripcion(adicionalDB.getDescripcion());
                adicionalOutDTO.setPrecio(adicional.getPrecio());
                adicionales.add(adicionalOutDTO);
            }
        }
        ventaOutDTO.setAdicionales(adicionales);
        List<PersonalizacionOutDTO> personalizaciones = new ArrayList<>();
        for (PersonalizacionDTO personalizacion : ventaDTO.getPersonalizaciones()) {
            Optional<Personalizacion> personalizacionO = this.personalizacionService.findOne(personalizacion.getId());
            PersonalizacionOutDTO personalizacionOutDTO = new PersonalizacionOutDTO();
            if (personalizacionO.isPresent()) {
                Personalizacion personalizacionDB = personalizacionO.get();
                personalizacionOutDTO.setId(personalizacionDB.getId());
                personalizacionOutDTO.setNombre(personalizacionDB.getNombre());
                personalizacionOutDTO.setDescripcion(personalizacionDB.getDescripcion());
                OpcionOutDTO opcionOutDTO = new OpcionOutDTO();
                Optional<Opcion> opcionO = this.opcionService.findOne(personalizacion.getId());
                if (opcionO.isEmpty()) {
                    continue;
                }
                Opcion opcionDB = opcionO.get();
                opcionOutDTO.setId(opcionDB.getId());
                opcionOutDTO.setNombre(opcionDB.getNombre());
                opcionOutDTO.setDescripcion(opcionDB.getDescripcion());
                opcionOutDTO.setPrecioAdicional(personalizacion.getPrecio());
                personalizacionOutDTO.setOpcion(opcionOutDTO);
            }
            personalizaciones.add(personalizacionOutDTO);
        }
        ventaOutDTO.setPersonalizaciones(personalizaciones);

        List<CaracteristicaOutDTO> caracteristicas = new ArrayList<>();
        for (Caracteristica caracteristica : dispositivo.getCaracteristicas()) {
            CaracteristicaOutDTO caracteristicaOutDTO = new CaracteristicaOutDTO();
            caracteristicaOutDTO.setId(caracteristica.getId());
            caracteristicaOutDTO.setNombre(caracteristica.getNombre());
            caracteristicaOutDTO.setDescripcion(caracteristica.getDescripcion());
            caracteristicas.add(caracteristicaOutDTO);
        }
        ventaOutDTO.setCatacteristicas(caracteristicas);
        return ventaOutDTO;
    }

    public VentaOutDTO getVentaOutDTODesdeVenta(Venta venta) {
        String ventaJson = venta.getVentaResultadoJson();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        VentaOutDTO ventaOutDTO = null;
        try {
            ventaOutDTO = objectMapper.readValue(ventaJson, VentaOutDTO.class);
        } catch (JsonMappingException e) {
            log.error(e.getMessage());
            return null;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
        return ventaOutDTO;
    }

    public List<VentaListaOutDTO> getVentasPorGrupo(Principal principal) {
        Grupo grupo = this.manejoSesionService.obtenerGrupoDePrincipal(principal);
        if (grupo == null) {
            return null;
        }
        List<Venta> ventas = this.ventaPersonalizadoRepository.findAllByGrupo(grupo);
        List<VentaListaOutDTO> ventasDTO = new ArrayList<>();
        for (Venta venta : ventas) {
            VentaListaOutDTO ventaOut = new VentaListaOutDTO();
            ventaOut.setIdVenta(venta.getIdVenta());
            ventaOut.setId(venta.getId());
            ventaOut.setCodigo(venta.getCodigo());
            ventaOut.setNombre(venta.getNombre());
            ventaOut.setDescription(venta.getDescripcion());
            ventaOut.setPrecio(venta.getPrecio());
            ventasDTO.add(ventaOut);
        }
        return ventasDTO;
    }

    public VentaOutDTO getVentaOutById(Principal principal, Long id) {
        Optional<Venta> venta = this.ventaPersonalizadoRepository.findById(id);
        VentaOutDTO ventaOutDTO = null;
        if (venta.isPresent()) {
            ventaOutDTO = this.getVentaOutDTODesdeVenta(venta.get());
        }
        return ventaOutDTO;
    }
}
