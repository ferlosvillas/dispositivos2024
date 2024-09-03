package ar.edu.um.programacion2.trabajo_final.service;

public interface ManejoSesionService {
    public boolean activarUsuario(ActivarUsuarioInDTO usuarioDTO, Principal principal) throws ActivarUsuarioException;

    public boolean revisarDatosUsuario(ActivarUsuarioInDTO usuarioDTO);

    public Grupo obtenerGrupoDePrincipal(Principal principal);
}
