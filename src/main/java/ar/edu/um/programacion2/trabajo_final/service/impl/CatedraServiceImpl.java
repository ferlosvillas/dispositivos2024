package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.Dispositivo;
import ar.edu.um.programacion2.trabajo_final.service.CatedraService;
import ar.edu.um.programacion2.trabajo_final.service.DispositivoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatedraServiceImpl implements CatedraService {

    @Autowired
    DispositivoService dispositivoService;

    public List<Dispositivo> getDispositivos() {
        List<Dispositivo> dispositivos = dispositivoService.findAll();
        return dispositivos;
    }
}
