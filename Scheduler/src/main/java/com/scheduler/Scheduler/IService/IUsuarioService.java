package com.scheduler.Scheduler.IService;

import java.util.List;
import java.util.Optional;

import com.scheduler.Scheduler.Entity.Usuario;


public interface IUsuarioService {

	Usuario saveUsuario(Usuario usuario);
    
    List<Usuario> getAllUsuarios();
    
    Optional<Usuario> getUsuarioById(Long id);
    
    Usuario getUsuarioByCorreoElectronico(String correoElectronico);
    
    Usuario getUsuarioByNumDocumento(String numDocumento);
    
    void deleteUsuario(Long id);
    
    void enviarRecordatorioCambioContraseña();
}
