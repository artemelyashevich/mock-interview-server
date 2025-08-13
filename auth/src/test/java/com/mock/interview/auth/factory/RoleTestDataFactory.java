package com.mock.interview.auth.factory;

import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import com.mock.interview.auth.infrastructure.persistence.entity.UserEntity;
import com.mock.interview.lib.model.RoleModel;

import java.util.Set;

public class RoleTestDataFactory {

    public static RoleEntity createRoleEntity(Long id, String name) {
        return RoleEntity.builder()
            .id(id)
            .name(name)
            .users(Set.of())
            .build();
    }

    public static RoleModel createRoleModel(Long id, String name) {
        return RoleModel.builder()
            .id(id)
            .name(name)
            .build();
    }

    public static RoleEntity createDefaultRoleEntity() {
        return createRoleEntity(1L, "ADMIN");
    }

    public static RoleModel createDefaultRoleModel() {
        return createRoleModel(1L, "ADMIN");
    }

    public static RoleEntity createRoleEntityWithUsers(Long id, String name, Set<UserEntity> users) {
        RoleEntity entity = createRoleEntity(id, name);
        entity.setUsers(users);
        return entity;
    }
}
