package com.mock.interview.auth.application.port.out;

import com.mock.interview.lib.model.RoleModel;

import java.util.List;

public interface RoleRepository {

  RoleModel findByName(String name);

  List<RoleModel> findAll();

  boolean existsByName(String name);

  RoleModel save(RoleModel roleModel);

  void delete(RoleModel roleModel);
}
