package com.tcc.qbeacon.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Bloco;

@Repository
@Transactional
public interface BlocoRepository extends JpaRepository<Bloco, Integer> {
	
	@Query(value = "SELECT * FROM BLOCO b "
			+ "WHERE b.id NOT IN "
			+ "(SELECT blocos_id FROM CAMPUS_BLOCOS)",
			nativeQuery=true)
	List<Bloco> blocosValidos();
	
}
