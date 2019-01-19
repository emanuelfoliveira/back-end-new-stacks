package br.com.efo.bens.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.efo.bens.ds.User;

public interface UserRepository extends MongoRepository<User, Integer>
{
    public User findByUserName(String userName);
    public List<User> findByEmail(String email);
}
