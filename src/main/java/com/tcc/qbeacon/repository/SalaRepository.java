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
			+ "WHERE s.id NOT IN "
			+ "(SELECT salas_id FROM BLOCO_SALAS)",
			nativeQuery=true)
	List<Sala> salasValidas();
}
