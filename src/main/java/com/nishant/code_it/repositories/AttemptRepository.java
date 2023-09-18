package com.nishant.code_it.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nishant.code_it.models.Attempt;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt,Long>{

}
