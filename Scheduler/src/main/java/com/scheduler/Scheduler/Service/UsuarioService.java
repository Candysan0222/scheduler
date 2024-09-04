package com.scheduler.Scheduler.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
    @Scheduled(fixedRate = 1800000) // Cada 30 minutos
    @Transactional
    public void enviarRecordatorioCambioContraseña() {
        // Obtener solo los usuarios que no han sido notificados
        List<Usuario> usuarios = repository.findByNotifiFalse();
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
            contenido = contenido.replace("{nombre_usuario}", usuario.getNumDocumento());
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

//Para probar fecha nacimiento , fecha 2005 y TI, cambio el cron por fixedRate = 10000
	@Scheduled(cron = "0 0 0 * * ?") // Se ejecuta todos los días a medianoche
    @Transactional
    public void verificarYNotificarCambioDocumento() {
        List<Usuario> usuarios = repository.findAll();

        for (Usuario usuario : usuarios) {
        	
        	String contenido = "";
        	try {
                contenido = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/CumpleEdad.html")));
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo HTML: " + e.getMessage());
            }
            LocalDate fechaNacimiento = usuario.getFechaNacimiento().toLocalDate();
            int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

            if (edad >= 18 && "TI".equals(usuario.getTipoDocumento())) {
                String asunto = "Actualización de Tipo de Documento Requerida";
                
                // Crear contexto para Thymeleaf
                contenido.replace("nombre_usuario", usuario.getNumDocumento());
                contenido.replace("enlace_cambio_documento", "https://tu-dominio.com/cambiar-tipo-documento");
                

                try {
                    enviarCorreo(usuario.getCorreoElectronico(), asunto, contenido);
                } catch (Exception e) {
                    throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
                }
            }
        }
    }
    
	//Para probarlo , creo un registro donde el fechaInicioSesion sea 2022 
	//pendiente de que el estado sea verdadero 
	@Scheduled(cron = "0 0 0 * * SUN") // Se ejecuta todos los domingos a medianoche
	@Transactional
	public void verificarEstadoUsuario() {
	    // Obtener la fecha actual menos un mes
	    LocalDateTime unMesAtras = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

	    // Obtener usuarios que no han iniciado sesión en el último mes
	    List<Usuario> usuarios = repository.findByFechaInicioSesionBefore(unMesAtras);

	    // Leer el contenido del archivo HTML
	    String contenidoPlantilla = "";
	    try {
	        contenidoPlantilla = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/Estado.html")));
	    } catch (IOException e) {
	        throw new RuntimeException("Error al leer el archivo HTML: " + e.getMessage());
	    }

	    for (Usuario usuario : usuarios) {
	        // Actualizar el estado a falso
	        usuario.setEstado(false);
	        repository.save(usuario);

	        // Reemplazar placeholders en el contenido
	        String contenido = contenidoPlantilla
	            .replace("{nombre_usuario}", usuario.getNumDocumento())
	            .replace("{enlace_soporte}", "https://tu-dominio.com/soporte"); // Cambia esto según sea necesario

	        // Enviar un correo notificando el cambio
	        String asunto = "Notificación de Inactividad";

	        try {
	            enviarCorreo(usuario.getCorreoElectronico(), asunto, contenido);
	        } catch (MessagingException e) {
	            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
	        }
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
