package com.mock.interview.auth.application.port.out;


import com.mock.interview.lib.model.UserModel;

public interface UserRepository {

  UserModel findById(Long id);

  UserModel findByEmail(String email);

  UserModel save(UserModel user);

  void delete(UserModel userModel);

  boolean existsByEmail(String email);
}
