package com.mock.interview.auth.application.port.out;

import com.mock.interview.lib.model.RoleModel;

import java.util.List;

public interface RoleRepository {

  RoleModel findByName(String name);

  List<RoleModel> findAll();

  RoleModel save(RoleModel roleModel);

  void delete(RoleModel roleModel);
}
