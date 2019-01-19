package br.com.efo.bens.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.efo.bens.ds.IdGen;
import br.com.efo.bens.repository.IdGenRepository;
import br.com.efo.bens.service.inter.IdGenService;

@Service("IdGenService")
public class IdGenServiceImpl implements IdGenService {
	@Autowired
	private IdGenRepository IdGenRepo;

	@Override
	public Integer nextId(String collection) {
		Optional<IdGen> optionalGenerator = IdGenRepo.findById(collection);
		if (!optionalGenerator.isPresent()) {
			IdGenRepo.save(new IdGen(collection, 0));
		}
		optionalGenerator = IdGenRepo.findById(collection);
		Integer result = optionalGenerator.get().getCurrentId();
		result++;
		optionalGenerator.get().setCurrentId(result);
		IdGenRepo.save(optionalGenerator.get());
		return result;
	}

	@Override
	public void delete() {
		IdGenRepo.deleteAll();
	}
}
