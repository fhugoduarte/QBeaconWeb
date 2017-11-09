package com.tcc.qbeacon.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Bloco;

@Repository
@Transactional
public interface BlocoRepository extends JpaRepository<Bloco, Integer> {

}
