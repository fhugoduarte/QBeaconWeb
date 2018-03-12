package com.tcc.qbeacon.datas;

import java.util.List;

public class UsuarioData {

	private Integer id;
	private String nome;
	private String email;
	private String senha;
	private List<Integer> turmas;
	
	public UsuarioData() {
		
	}
	
	public UsuarioData(Integer id, String nome, String email, String senha, List<Integer> turmas) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.turmas = turmas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Integer> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Integer> turmas) {
		this.turmas = turmas;
	}
	
}
