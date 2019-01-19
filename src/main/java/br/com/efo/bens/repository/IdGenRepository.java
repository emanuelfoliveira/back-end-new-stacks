package br.com.efo.bens.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.efo.bens.ds.IdGen;

public interface IdGenRepository extends MongoRepository<IdGen, String> {

}
