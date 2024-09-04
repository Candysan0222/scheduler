package com.scheduler.Scheduler.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Scheduler.Entity.Usuario;
import com.scheduler.Scheduler.Service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@PostMapping("/recordar-cambio-contrasena/{id}")
    public void recordarCambioContrasena() {
        service.enviarRecordatorioCambioContraseña();
    }
	
	@PostMapping
	public Usuario saveUsuario(@RequestBody Usuario usuario) {
		return service.saveUsuario(usuario);
	

	}
}
