package com.grp.trackDesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grp.trackDesk.models.Project;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	  boolean existsByName(String name);
}
