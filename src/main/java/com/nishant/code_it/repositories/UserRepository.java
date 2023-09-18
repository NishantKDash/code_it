package com.nishant.code_it.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nishant.code_it.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{

	public UserEntity findByusername(String username);
}
