package com.tcc.qbeacon.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Campus;

@Repository
@Transactional
public interface CampusRepository extends JpaRepository<Campus, Integer> {

}
