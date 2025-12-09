package com.grp.trackDesk.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.grp.trackDesk.exception.DuplicateResourceException;
import com.grp.trackDesk.exception.ResourceNotFoundException;
import com.grp.trackDesk.models.Project;
import com.grp.trackDesk.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projRepo;

	public List<Project> getAllProjects() {
		return projRepo.findAll();

	}
	
	public Project getProjectById(Long id) {
		return projRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("project","id",id));
	}
	
	
	public Project saveProject(Project project) {
		try {
			if (projRepo.existsByName(project.getName())) {
                throw new DuplicateResourceException("Projet", "name", project.getName());
            }
			return projRepo.save(project);
		} catch (DataIntegrityViolationException ex) {
			log.error("Data integrity violation while creating project: {}", ex.getMessage());
            throw new DuplicateResourceException("Projet", "name", project.getName());
			// TODO: handle exception
		}
		
	}
	
	public void deleteRecord(Long id) {
		projRepo.deleteById(id);
	}
}
