package com.mock.interview.auth.infrastructure.persistence.repository;

import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
  Optional<RoleEntity> findByName(String name);

  @Query("select (count(r) > 0) from RoleEntity r where r.name = :name")
  boolean existsByName(String name);
}
