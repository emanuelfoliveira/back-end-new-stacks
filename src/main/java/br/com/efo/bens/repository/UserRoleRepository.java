package br.com.efo.bens.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.efo.bens.ds.UserRole;

public interface UserRoleRepository extends MongoRepository<UserRole, Integer>
{
    public UserRole findByName(String name);
}
