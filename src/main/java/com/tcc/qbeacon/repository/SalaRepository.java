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
	//Retorna todas as salas que não possuem beacon.
	@Query(value = "SELECT * FROM SALA s "
			+ "WHERE s.beacon_id IS NULL",
			nativeQuery=true)
	List<Sala> salasValidas();
	
	//Retorna todas as salas que tem horário vago no dia da semana e no periodo passado como parametro.
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
	
	@Query(value = "SELECT * FROM SALA s "
			+ "WHERE s.id IN("
			+ "		SELECT sala_id FROM SALA_RESERVAS sr WHERE sr.reservas_id IN("
			+ "			SELECT reservas_id FROM HORARIO_RESERVAS hr WHERE hr.horario_id IN("
			+ "				SELECT id FROM HORARIO h WHERE h.periodo = ?1 AND h.dia_semana = ?2"
			+ "				)"
			+ "			)"
			+ "		)",
			nativeQuery=true)
	List<Sala> salasComAula(String hora, String diaSemana);
	
	@Query(value = "SELECT * FROM SALA s "
			+ "WHERE s.id IN("
			+ "		SELECT sala_id FROM SALA_RESERVAS sr WHERE sr.reservas_id IN("
			+ "			SELECT reservas_id FROM HORARIO_RESERVAS hr WHERE hr.horario_id IN("
			+ "				SELECT id FROM HORARIO h WHERE h.dia_semana = ?1"
			+ "				)"
			+ "			)"
			+ "		)",
			nativeQuery=true)
	List<Sala> salasComAula(String diaSemana);
	
}
