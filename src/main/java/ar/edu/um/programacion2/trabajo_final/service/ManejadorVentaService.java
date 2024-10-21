package ar.edu.um.programacion2.trabajo_final.service;

import ar.edu.um.programacion2.trabajo_final.domain.DTO.CrearVentaDTO;
import ar.edu.um.programacion2.trabajo_final.domain.DTO.VentaListaOutDTO;
import ar.edu.um.programacion2.trabajo_final.domain.DTO.VentaOutDTO;
import ar.edu.um.programacion2.trabajo_final.domain.Venta;
import java.security.Principal;
import java.util.List;

public interface ManejadorVentaService {
    public Venta guardarVenta(CrearVentaDTO CrearVentaDTO, Principal principal);

    public VentaOutDTO getVentaOutDTODesdeVenta(Venta venta);

    public List<VentaListaOutDTO> getVentasPorGrupo(Principal principal);

    public VentaOutDTO getVentaOutById(Principal principal, Long id);
}
