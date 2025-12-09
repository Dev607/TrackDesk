package com.grp.trackDesk.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grp.trackDesk.models.Project;
import com.grp.trackDesk.models.TrDomain;
import com.grp.trackDesk.repository.DomainRepository;
import com.grp.trackDesk.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DomainService {
	
private final DomainRepository  domainRepo;
	
	public List<TrDomain> getAllDomains() {
		return domainRepo.findAll();
	}
	
	public TrDomain getDomainById(Long id) {
		return domainRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Domain not found"));
	}
	
	public TrDomain saveDomain(TrDomain trDomain) {
		return domainRepo.save(trDomain);
	}
	
	public void deleteRecord(Long id) {
		domainRepo.deleteById(id);
	}

}
