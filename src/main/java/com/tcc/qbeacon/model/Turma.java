package com.tcc.qbeacon.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String professor;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Reserva reserva1;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Reserva reserva2;
	
	@ManyToMany
	private List<Usuario> alunos;
	
	@ManyToOne
	private Disciplina disciplina;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Aula> aulas;
	
	public Turma() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public Reserva getReserva1() {
		return reserva1;
	}

	public void setReserva1(Reserva reserva1) {
		this.reserva1 = reserva1;
	}
	
	public Reserva getReserva2() {
		return reserva2;
	}

	public void setReserva2(Reserva reserva2) {
		this.reserva2 = reserva2;
	}

	public List<Usuario> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Usuario> alunos) {
		this.alunos = alunos;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public void setAulas(List<Aula> aulas) {
		this.aulas = aulas;
	}
				
}
