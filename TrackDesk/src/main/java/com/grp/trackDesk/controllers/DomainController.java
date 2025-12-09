package com.grp.trackDesk.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grp.trackDesk.models.Project;
import com.grp.trackDesk.models.TrDomain;
import com.grp.trackDesk.services.DomainService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/domain")
public class DomainController {

	private final DomainService domainService;
	
	@GetMapping
	public ResponseEntity<List<TrDomain>> getAllDomains(){
		List<TrDomain> trDomains = domainService.getAllDomains();
		return ResponseEntity.ok(trDomains);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TrDomain> getDomainById(@PathVariable Long id) {
		TrDomain domain = domainService.getDomainById(id);
		return ResponseEntity.ok(domain);

	}

	@PostMapping
	public ResponseEntity<TrDomain> createDomain(@Valid @RequestBody TrDomain prj) {

		TrDomain saved = domainService.saveDomain(prj);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TrDomain> updateDomain(@PathVariable Long id, @RequestBody Project prj) {

		TrDomain existing = domainService.getDomainById(id);
		existing.setName(prj.getName());
		existing.setDescription(prj.getDescription());

		TrDomain updated = domainService.saveDomain(existing);

		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
		domainService.deleteRecord(id);
		return ResponseEntity.noContent().build(); // 204 No Content

	}
	
}
