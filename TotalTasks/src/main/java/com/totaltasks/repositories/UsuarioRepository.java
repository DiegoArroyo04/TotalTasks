package com.totaltasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

	UsuarioEntity findByusuario(String usuario);

	UsuarioEntity findByemail(String email);

}