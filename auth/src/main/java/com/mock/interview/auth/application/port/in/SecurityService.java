package com.mock.interview.auth.application.port.in;

import com.mock.interview.lib.model.UserModel;
import org.springframework.security.core.Authentication;

public interface SecurityService {

    UserModel findCurrentUser();

    UserModel authenticate(Authentication authentication);
}
