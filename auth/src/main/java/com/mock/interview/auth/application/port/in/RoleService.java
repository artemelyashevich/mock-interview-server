package com.mock.interview.auth.application.port.in;

import com.mock.interview.lib.model.RoleModel;

import java.util.List;

public interface RoleService {

    List<RoleModel> findAll();

    RoleModel findByName(String name);

    RoleModel save(RoleModel roleModel);

    void delete(RoleModel roleModel);
}
