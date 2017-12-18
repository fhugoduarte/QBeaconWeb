package com.tcc.qbeacon.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Campus;

@Repository
@Transactional
public interface CampusRepository extends JpaRepository<Campus, Integer> {
	@Query(value = "SELECT * FROM CAMPUS c "
			+ "WHERE c.id NOT IN "
			+ "(SELECT campus_id FROM INSTITUICAO_CAMPUS)",
			nativeQuery=true)
	List<Campus> campusValidos();
	
}
