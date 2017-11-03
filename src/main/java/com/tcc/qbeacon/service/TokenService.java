package com.tcc.qbeacon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Token;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.repository.TokenRepository;

@Service
@ComponentScan("com.tcc.qbeacon.service")
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;
	
	public Token buscarPorUsuario(Usuario usuario) {
		return tokenRepository.findByUsuario(usuario);
	}
	
	public Token buscar(String token) {
		return tokenRepository.findOne(token);
	}

	public boolean existe(String token) {
		return tokenRepository.exists(token);
	}

	public void salvar(Token token) {
		tokenRepository.save(token);
	}

	public void deletar(Token token) {
		tokenRepository.delete(token);
	}
	
}
