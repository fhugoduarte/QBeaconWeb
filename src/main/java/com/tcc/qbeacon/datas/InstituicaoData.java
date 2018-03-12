package com.tcc.qbeacon.datas;

import java.util.List;

public class InstituicaoData {
	
	private Integer id;
	private String nome;
	private List<CampusData> campus;
	
	public InstituicaoData() {
		
	}
	
	public InstituicaoData(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
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

	public List<CampusData> getCampus() {
		return campus;
	}

	public void setCampus(List<CampusData> campus) {
		this.campus = campus;
	}
	
}
