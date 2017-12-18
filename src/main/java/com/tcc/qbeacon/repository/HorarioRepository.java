package com.tcc.qbeacon.repository;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Horario;

@Repository
@Transactional
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
	
	@Query(value = "SELECT * FROM HORARIO h "
			+ "WHERE h.dia_semana = ?1 AND h.periodo = ?2",
			nativeQuery=true)
	Horario igual(String diaSemana, String periodo);
	
}
