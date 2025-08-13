package com.mock.interview.auth.application.port.out;


import com.mock.interview.lib.model.UserModel;

public interface UserRepository {

    UserModel findById(Long id);

    UserModel findByLogin(String login);

    UserModel save(UserModel user);

    void delete(UserModel userModel);

    boolean existsByLogin(String email);
}
