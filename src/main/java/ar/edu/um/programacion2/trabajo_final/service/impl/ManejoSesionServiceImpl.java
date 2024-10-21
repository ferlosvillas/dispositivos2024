package ar.edu.um.programacion2.trabajo_final.service.impl;

import ar.edu.um.programacion2.trabajo_final.domain.DTO.ActivarUsuarioInDTO;
import ar.edu.um.programacion2.trabajo_final.domain.Grupo;
import ar.edu.um.programacion2.trabajo_final.domain.User;
import ar.edu.um.programacion2.trabajo_final.excepciones.ActivarUsuarioException;
import ar.edu.um.programacion2.trabajo_final.repository.GrupoCustomizedRepository;
import ar.edu.um.programacion2.trabajo_final.repository.GrupoRepository;
import ar.edu.um.programacion2.trabajo_final.repository.UserCustomizedRepository;
import ar.edu.um.programacion2.trabajo_final.repository.UserRepository;
import ar.edu.um.programacion2.trabajo_final.service.ManejoSesionService;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManejoSesionServiceImpl implements ManejoSesionService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserCustomizedRepository userCustomizedRepository;

    @Autowired
    protected GrupoRepository grupoRepository;

    @Autowired
    protected GrupoCustomizedRepository grupoCustomizedRepository;

    @Override
    public boolean activarUsuario(ActivarUsuarioInDTO usuarioDTO, Principal principal) throws ActivarUsuarioException {
        User user = this.findUserByEmailAndLogin(usuarioDTO.getEmail(), usuarioDTO.getLogin());
        if (user != null) {
            if (user.isActivated()) {
                //Usuario activado
                throw new ActivarUsuarioException("EL usuario ya ha sido activado");
            } else {
                // Usuario sin activar
                user.setActivated(true);
                Grupo grupo = new Grupo();
                grupo.setNombres(usuarioDTO.getNombres());
                grupo.setDescripcion(usuarioDTO.getDescripcion());
                UUID uuid = UUID.randomUUID();
                grupo.setIdGrupo(uuid);
                grupo.setUser(user);
                this.userRepository.save(user);
                this.grupoRepository.save(grupo);
                return true;
            }
        } else {
            //Usuario no encontrado
            throw new ActivarUsuarioException("EL usuario no existe en la base de datos");
        }
    }

    @Override
    public boolean revisarDatosUsuario(ActivarUsuarioInDTO usuarioDTO) {
        if (
            StringUtils.isNoneBlank(usuarioDTO.getEmail()) &&
            StringUtils.isNoneBlank(usuarioDTO.getLogin()) &&
            StringUtils.isNoneBlank(usuarioDTO.getNombres()) &&
            StringUtils.isNoneBlank(usuarioDTO.getDescripcion())
        ) {
            return true;
        }
        return false;
    }

    @Override
    public Grupo obtenerGrupoDePrincipal(Principal principal) {
        String name = principal.getName();
        Optional<User> user = this.userRepository.findOneByLogin(name);
        if (user.isPresent()) {
            Optional<Grupo> grupo = this.grupoCustomizedRepository.findOneByUser(user.get());
            if (grupo.isPresent()) {
                return grupo.get();
            }
        }
        return null;
    }

    public User findUserByEmailAndLogin(String email, String login) {
        Optional<User> userO = this.userCustomizedRepository.findOneByEmailIgnoreCaseAndLogin(email, login);
        if (userO.isPresent()) {
            return userO.get();
        }
        return null;
    }
}
