package br.com.efo.bens.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.efo.bens.ds.UserRole;
import br.com.efo.bens.repository.UserRoleRepository;
import br.com.efo.bens.service.inter.IdGenService;
import br.com.efo.bens.service.inter.UserRoleService;

@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	private UserRoleRepository userRoleRepo;
	@Autowired
	private IdGenService idGenService;

	@Override
	public UserRole save(UserRole userRole) {
		if (userRole.getId() == null) {
			UserRole userRoleSame = userRoleRepo.findByName(userRole.getName());
			if (userRoleSame != null) {
				userRole.setId(userRoleSame.getId());
			} else {
				userRole.setId(idGenService.nextId("UserRole"));
			}
		}
		userRole = userRoleRepo.save(userRole);
		return userRole;
	}

	@Override
	public UserRole query(String userRole) {
		return userRoleRepo.findByName(userRole);
	}

	@Override
	public Optional<UserRole> query(Integer id) {
		return userRoleRepo.findById(id);
	}

	@Override
	public void delete() {
		userRoleRepo.deleteAll();
	}

	@Override
	public void delete(Integer id) {
		userRoleRepo.deleteById(id);
	}

	@Override
	public List<UserRole> query() {
		return userRoleRepo.findAll();
	}

}
