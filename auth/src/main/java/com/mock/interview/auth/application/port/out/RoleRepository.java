package com.mock.interview.auth.application.port.out;

import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import com.mock.interview.lib.model.RoleModel;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Optional<RoleEntity> findByName(String name);

    List<RoleModel> findAll();

    boolean existsByName(String name);

    RoleModel save(RoleModel roleModel);

    void delete(RoleModel roleModel);
}
