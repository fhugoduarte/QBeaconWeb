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
	@Query("select c "
			+ "from Campus c "
			+ "c.id not in  (select campus_id from INSTITUICAO_CAMPUS)")
	List<Campus> campusValidos();
}
