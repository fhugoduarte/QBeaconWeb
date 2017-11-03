package com.tcc.qbeacon.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Token;
import com.tcc.qbeacon.model.Usuario;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, String>{

	Token findByUsuario(Usuario usuario);
	
}
