package com.selecaoEstagio.cadastro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.selecaoEstagio.cadastro.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	Object findByUsername(String username);

	//Users findByUuid(String uuid);
	
	Optional<Users> findByUuid(String uuid);

	void deleteByUuid(String uuid);


}
