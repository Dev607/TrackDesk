package com.grp.trackDesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grp.trackDesk.models.TrDomain;


@Repository
public interface DomainRepository extends JpaRepository<TrDomain, Long>{

}
