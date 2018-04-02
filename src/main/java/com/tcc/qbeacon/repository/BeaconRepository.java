package com.tcc.qbeacon.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Beacon;

@Repository
@Transactional
public interface BeaconRepository extends JpaRepository<Beacon, Integer> {
	
	//Retorna a lista de beacons que não tem sala.
	@Query(value = "SELECT * FROM BEACON b "
			+ "WHERE b.sala_id IS NULL ", nativeQuery=true)
	List<Beacon> beaconsValidos();
	
}
