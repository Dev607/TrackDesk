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
import com.grp.trackDesk.services.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects() {
		List<Project> projects = projectService.getAllProjects();

		return ResponseEntity.ok(projects);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
		Project category = projectService.getProjectById(id);
		return ResponseEntity.ok(category);

	}

	@PostMapping
	public ResponseEntity<Project> createProject(@Valid @RequestBody Project prj) {

		Project saved = projectService.saveProject(prj);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project prj) {

		Project existing = projectService.getProjectById(id);
		existing.setName(prj.getName());
		existing.setDescription(prj.getDescription());

		Project updated = projectService.saveProject(existing);

		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		projectService.deleteRecord(id);
		return ResponseEntity.noContent().build(); // 204 No Content

	}

}
