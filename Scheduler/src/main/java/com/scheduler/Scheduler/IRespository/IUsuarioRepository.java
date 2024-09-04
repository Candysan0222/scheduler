package com.scheduler.Scheduler.IRespository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scheduler.Scheduler.Entity.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByCorreoElectronico(String correoElectronico);
	Usuario findByNumDocumento(String numDocumento);
}
