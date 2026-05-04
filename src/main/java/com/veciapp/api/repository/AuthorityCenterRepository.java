package com.veciapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veciapp.api.entity.AuthorityCenter;

public interface AuthorityCenterRepository extends JpaRepository<AuthorityCenter, Long> {

    List<AuthorityCenter> findByActiveTrueOrderByNameAsc();
}

