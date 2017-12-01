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
	
	@Query(value = "SELECT * FROM SALA s "
			+ "WHERE s.beacon_id IS NULL",
			nativeQuery=true)
	List<Turma> salasSemDuasReservas();
	
}
