package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.datas.UsuarioData;
import com.tcc.qbeacon.model.Papel;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.UsuarioService;
import com.tcc.qbeacon.util.Constants;
import com.tcc.qbeacon.util.MensagemRetorno;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {
	
	@Autowired
	UsuarioService usuarioService;
	
	@PostMapping(path="/logar")
	public MensagemRetorno logar(@RequestBody Usuario usuario) {
		System.err.println("TESTE CADSATRAR");
		if(usuarioService.logar(usuario))
			return new MensagemRetorno(Constants.SUCESSO_LOGIN_USUARIO);
		else
			return new MensagemRetorno(Constants.ERRO_EMAIL_SENHA);
	}
	
	@PostMapping
	public MensagemRetorno cadastrar(@RequestBody @Valid Usuario usuario) throws Exception{
		try {
			System.err.println("TESTE CADSATRAR");
			usuario.setPapel(Papel.ALUNO);
			usuarioService.salvarUsuario(usuario);
			return new MensagemRetorno(Constants.SUCESSO_CADASTRO_USUARIO);
		} catch (Exception e) {
			throw new Exception(Constants.ERRO_CADASTRO_USUARIO);
		}
	}
	
	@GetMapping
	public ResponseEntity<UsuarioData> buscar() throws Exception{
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		List<Integer> turmas = new ArrayList<>();
		
		if(usuarioLogado.getTurmas() != null) {
			for (Turma turma : usuarioLogado.getTurmas()) {
				turmas.add(turma.getId());			
			}
		}
		
		UsuarioData usuario = new UsuarioData(usuarioLogado.getId(), 
			usuarioLogado.getNome(), 
			usuarioLogado.getEmail(), 
			usuarioLogado.getSenha(),
			turmas);
		
		return new ResponseEntity<UsuarioData>(usuario, HttpStatus.OK); 
	}
	
}
