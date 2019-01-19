package br.com.efo.bens.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.efo.bens.ds.User;
import br.com.efo.bens.repository.UserRepository;
import br.com.efo.bens.service.inter.IdGenService;
import br.com.efo.bens.service.inter.UserService;

@Service("userServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private IdGenService idGenService;

	@Override
	public User save(User user) {
		if (user.getId() == null) {
			User userSameUsername = userRepo.findByUserName(user.getUserName());
			if (userSameUsername != null) {
				user.setId(userSameUsername.getId());
				if (user.getPasswordText() == null) {
					user.setOriginalPass(true);
					user.setPasswordText(userSameUsername.getPasswordText());
				}
				return userRepo.save(user);
			}
			user.setId(idGenService.nextId("User"));
			if (user.getPasswordText() == null) {
				user.setPasswordText("BENS_PASS");
			}
			user = userRepo.save(user);
			return user;
		}
		User userOld = userRepo.findById(user.getId()).get();
		if (user.getPasswordText() == null) {
			user.setOriginalPass(true);
			user.setPasswordText(userOld.getPasswordText());
		}
		return userRepo.save(user);
	}

	@Override
	public Optional<User> getUserById(Integer id) {
		if (id != null) {
			return userRepo.findById(id);
		}
		return Optional.empty();
	}

	@Override
	public User queryByUserName(String userName) {
		return userRepo.findByUserName(userName);
	}

	@Override
	public List<User> queryAll() {
		return makeCollection(userRepo.findAll());
	}

	private List<User> makeCollection(Iterable<User> iter) {
		List<User> list = new LinkedList<User>();
		for (User item : iter) {
			list.add(item);
		}
		return list;
	}

	@Override
	public void delete() {
		userRepo.deleteAll();
	}

	@Override
	public void deleteById(Integer id) {
		userRepo.deleteById(id);
	}

	@Override
	public User query(Integer userId) {
		return userRepo.findById(userId).get();
	}

	@Override
	public User queryByEmail(String email) {
		List<User> users = userRepo.findByEmail(email);
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

}
