package com.scheduler.Scheduler.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "tipo_documento", nullable = false, unique = false, length = 30)
	private String tipoDocumento;
	
	@Column(name = "num_documento", nullable = false, unique = true, length = 30)
	private String numDocumento;
	
	@Column(name = "fecha_nacimiento", nullable = false, unique = false, length = 30)
	private LocalDateTime fechaNacimiento;
	
	@Column(name = "contraseña", nullable = false, unique = false, length = 30)
	private String contraseña;
	
	@Column(name = "fecha_contra", nullable = false, unique = true, length = 30)
	private LocalDateTime fechaContra;
	
	@Column(name = "fecha_inicio_sesion", nullable = false, unique = true, length = 30)
	private LocalDateTime fechaInicioSesion;
	
	@Column(name = "correo_electronico", nullable = false, unique = true, length = 50)
	private String correoElectronico;
	
	@Column(name = "notifi", nullable = false)
	private boolean notifi;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public LocalDateTime getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDateTime fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public LocalDateTime getFechaContra() {
		return fechaContra;
	}

	public void setFechaContra(LocalDateTime fechaContra) {
		this.fechaContra = fechaContra;
	}

	public LocalDateTime getFechaInicioSesion() {
		return fechaInicioSesion;
	}

	public void setFechaInicioSesion(LocalDateTime fechaInicioSesion) {
		this.fechaInicioSesion = fechaInicioSesion;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public boolean isNotifi() {
		return notifi;
	}

	public void setNotifi(boolean notifi) {
		this.notifi = notifi;
	}

	
}
