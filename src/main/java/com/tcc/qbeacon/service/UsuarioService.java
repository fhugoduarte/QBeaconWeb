package com.tcc.qbeacon.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public String criptografarSenha(String senha) {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[];
			try {
				messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
				StringBuilder hexString = new StringBuilder();
				for (byte b : messageDigest) {
				  hexString.append(String.format("%02X", 0xFF & b));
				}
				return hexString.toString();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return "erro";
	}
	
	public boolean validarSenha(String senhaCrip, String senha) {
		return criptografarSenha(senha).equals(senhaCrip);
	}
	
	public Usuario logar(String email, String senha) {
		Usuario userBanco = usuarioRepository.findByEmail(email);
		if(userBanco != null && validarSenha(userBanco.getSenha(), senha)) {
			return userBanco;
		}
		return null;
	}
	
	public Usuario salvarUsuario(Usuario usuario) {
		usuario.setSenha(this.criptografarSenha(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}

}
