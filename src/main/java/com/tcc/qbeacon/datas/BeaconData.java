package com.tcc.qbeacon.datas;

public class BeaconData {
	
	private Integer id;
	private String nome;
	private Integer sala;
	
	public BeaconData() {
		
	}
	
	public BeaconData(Integer id, String nome, Integer sala) {
		super();
		this.id = id;
		this.nome = nome;
		this.sala = sala;
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

	public Integer getSala() {
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
	}
	
}
