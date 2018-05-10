package com.tcc.qbeacon.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Reserva;

@Repository
@Transactional
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	//Retorna todas as reservas de um determinado dia da semana.
	@Query(value = "SELECT * FROM RESERVA r "
			+ "WHERE r.id IN ("
			+ "	SELECT reservas_id FROM HORARIO_RESERVAS hr WHERE hr.horario_id IN ("
			+ "		SELECT id FROM HORARIO h WHERE h.dia_semana = ?1"
			+ "		)"
			+ ")",
			nativeQuery=true)
	List<Reserva> buscarReservas(String diaSemana);
}
