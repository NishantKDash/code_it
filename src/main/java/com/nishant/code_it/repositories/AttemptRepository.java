package com.nishant.code_it.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nishant.code_it.models.Attempt;
import com.nishant.code_it.models.UserEntity;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt,Long>{
	
	public List<Attempt> getAttemptsByUser(UserEntity user);

}
