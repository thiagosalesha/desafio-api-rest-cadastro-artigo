package com.audax.cadastro.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.audax.cadastro.dto.UsersDTO;
import com.audax.cadastro.dto.form.UsersForm;
import com.audax.cadastro.model.Users;
import com.audax.cadastro.repository.UsersRepository;

@RestController
@RequestMapping("user")
public class UsersController {

	@Autowired
	UsersRepository usersRepository;

	@GetMapping
	public ResponseEntity<List<UsersDTO>> list() {

		List<Users> lista = usersRepository.findAll();
		return ResponseEntity.ok(UsersDTO.toUsers(lista));
	}
	
	@GetMapping("/{uuid}")
	public ResponseEntity<UsersDTO> list(@PathVariable String uuid) {
		Optional<Users> list = usersRepository.findByUuid(uuid);
		if(!list.isEmpty()) {
			return ResponseEntity.ok(new UsersDTO(list.get()));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
	}

	@PostMapping
	public ResponseEntity<UsersDTO> register(@RequestBody @Valid UsersForm usersForm) {
		Users user = new Users(usersForm);
		usersRepository.save(user);
		return ResponseEntity.created(null).body(new UsersDTO(user));
		
	}
	
	@PutMapping("/{uuid}")
	@Transactional
	public ResponseEntity<UsersDTO> update(@PathVariable String uuid, @RequestBody @Valid UsersForm usersForm) {
		Optional<Users> findByUuid = usersRepository.findByUuid(uuid);
		if (findByUuid.isPresent()) {
			Users atualizar = usersForm.atualizar(uuid, usersRepository);
			return ResponseEntity.ok(new UsersDTO(atualizar));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
	}
	
	@DeleteMapping("/{uuid}")
	public ResponseEntity delete(@PathVariable String uuid, HttpServletResponse response) {
		Optional<Users> deletar = usersRepository.findByUuid(uuid);
		if (deletar.isPresent()) {
			usersRepository.delete(deletar.get());
			return  ResponseEntity.noContent().build();	
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
	}
}
