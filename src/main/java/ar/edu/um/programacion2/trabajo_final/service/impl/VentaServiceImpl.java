package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import ar.edu.um.programacion2.trabajo_final.repository.VentaRepository;
import ar.edu.um.programacion2.trabajo_final.service.VentaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.trabajo_final.domain.Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private static final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta save(Venta venta) {
        log.debug("Request to save Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    @Override
    public Venta update(Venta venta) {
        log.debug("Request to update Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> partialUpdate(Venta venta) {
        log.debug("Request to partially update Venta : {}", venta);

        return ventaRepository
            .findById(venta.getId())
            .map(existingVenta -> {
                if (venta.getIdVenta() != null) {
                    existingVenta.setIdVenta(venta.getIdVenta());
                }
                if (venta.getCodigo() != null) {
                    existingVenta.setCodigo(venta.getCodigo());
                }
                if (venta.getNombre() != null) {
                    existingVenta.setNombre(venta.getNombre());
                }
                if (venta.getDescripcion() != null) {
                    existingVenta.setDescripcion(venta.getDescripcion());
                }
                if (venta.getPrecio() != null) {
                    existingVenta.setPrecio(venta.getPrecio());
                }
                if (venta.getVentaPedidoJson() != null) {
                    existingVenta.setVentaPedidoJson(venta.getVentaPedidoJson());
                }
                if (venta.getVentaResultadoJson() != null) {
                    existingVenta.setVentaResultadoJson(venta.getVentaResultadoJson());
                }

                return existingVenta;
            })
            .map(ventaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findAll() {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAll();
    }

    public Page<Venta> findAllWithEagerRelationships(Pageable pageable) {
        return ventaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
