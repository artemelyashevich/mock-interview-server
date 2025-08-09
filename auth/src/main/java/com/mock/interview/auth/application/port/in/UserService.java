package com.mock.interview.auth.application.port.in;

import com.mock.interview.lib.model.UserModel;

public interface UserService {

    UserModel findById(Long id);

    UserModel findByLogin(String login);

    UserModel save(UserModel user);

    void delete(UserModel userModel);

    UserModel update(UserModel user);
}
