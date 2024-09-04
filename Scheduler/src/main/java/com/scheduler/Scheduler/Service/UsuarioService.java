package com.scheduler.Scheduler.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.scheduler.Scheduler.Entity.Usuario;
import com.scheduler.Scheduler.IRespository.IUsuarioRepository;
import com.scheduler.Scheduler.IService.IUsuarioService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements IUsuarioService{

	@Autowired
	private IUsuarioRepository repository;
	
	@Autowired
    private JavaMailSender mailSender;
	
	
	@Override
	@Scheduled(fixedRate = 1800000) // 30 minutos
	@Transactional
	public void enviarRecordatorioCambioContraseña() {
	    List<Usuario> usuarios = repository.findAll();
	    String asunto = "Solicitud de cambio de contraseña de usuario";
	    
	    for (Usuario usuario : usuarios) {
	        // Leer el contenido del archivo HTML
	        String contenido = "";
	        try {
	            contenido = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/Correo.html")));
	        } catch (IOException e) {
	            throw new RuntimeException("Error al leer el archivo HTML: " + e.getMessage());
	        }

	        // Reemplazar placeholders en el contenido
	        contenido = contenido.replace("{nombre_usuario}", String.valueOf(usuario.getNumDocumento()));
	        contenido = contenido.replace("{enlace_cambio_contraseña}", "https://tu-dominio.com/cambiar-contrasena");

	        // Enviar el correo
	        try {
	            enviarCorreo(usuario.getCorreoElectronico(), asunto, contenido);
	        } catch (MessagingException e) {
	            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
	        }

	        // Actualizar el estado de notifi a verdadero
	        usuario.setNotifi(true);
	        repository.save(usuario);
	    }
	}
	
	private void enviarCorreo(String destinatario, String asunto, String contenido) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenido, true); // El true indica que el contenido es HTML

        mailSender.send(mensaje);
    }

	@Override
	public Usuario saveUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return repository.save(usuario);
	}

	@Override
	public List<Usuario> getAllUsuarios() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Optional<Usuario> getUsuarioById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public Usuario getUsuarioByCorreoElectronico(String correoElectronico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioByNumDocumento(String numDocumento) {
		// TODO Auto-generated method stub
		return repository.findByNumDocumento(numDocumento);
	}

	@Override
	public void deleteUsuario(Long id) {
		repository.deleteById(id);
		
	}
}
