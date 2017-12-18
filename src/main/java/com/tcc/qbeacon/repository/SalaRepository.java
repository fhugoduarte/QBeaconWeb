package com.tcc.qbeacon.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Sala;

@Repository
@Transactional
public interface SalaRepository extends JpaRepository<Sala, Integer> {
	@Query(value = "SELECT * FROM SALA s "
			+ "WHERE s.beacon_id IS NULL",
			nativeQuery=true)
	List<Sala> salasValidas();
	
	@Query(value = "SELECT * FROM SALA s "
			+ "WHERE s.id NOT IN ("
			+ "		SELECT sala_id FROM SALA_RESERVAS sr WHERE sr.reservas_id NOT IN ("
			+ " 		SELECT reservas_id FROM HORARIO_RESERVAS hr WHERE hr.horario_id NOT IN ("
			+ " 			SELECT id FROM HORARIO h WHERE h.dia_semana = ?1 AND h.periodo = ?2"
			+ "			)"
			+ "		)"
			+ ")",
			nativeQuery=true)
	List<Sala> salasVagas(String diaSemana, String periodo);
	
	/*select * from sala s where s.id not in (
	select sala_id from sala_reservas sr where sr.reservas_id not in (
		select reservas_id from horario_reservas hr where hr.horario_id not in (
			select id from horario h where h.dia_semana = 'Segunda-Feira' and h.horario = '08:00 às 10:00')
		)
	);*/
}
