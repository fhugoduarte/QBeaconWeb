package com.tcc.qbeacon.datas;

public class SalaData {

	private Integer id;
	private String nome;
	private String bloco;
	private String campus;
	private String instituicao;
	private Integer beacon;

	public SalaData() {
		
	}
	
	public SalaData(Integer id, String nome, String bloco, String campus, 
				String instituicao, Integer beacon) {
		super();
		this.id = id;
		this.nome = nome;
		this.bloco = bloco;
		this.campus = campus;
		this.instituicao = instituicao;
		this.beacon = beacon;
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

	public String getBloco() {
		return bloco;
	}

	public void setBloco(String bloco) {
		this.bloco = bloco;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public Integer getBeacon() {
		return beacon;
	}

	public void setBeacon(Integer beacon) {
		this.beacon = beacon;
	}
		
}
