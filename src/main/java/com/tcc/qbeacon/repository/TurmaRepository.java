package com.tcc.qbeacon.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Turma;

@Repository
@Transactional
public interface TurmaRepository extends JpaRepository<Turma, Integer>{
	
	//Retorna todas as turmas que não possuem as duas reservas cadastradas.
	@Query(value = "SELECT * FROM TURMA t "
			+ "WHERE t.reserva1_id IS NULL OR t.reserva2_id IS NULL",
			nativeQuery=true)
	List<Turma> turmasReservaveis();
	
}
