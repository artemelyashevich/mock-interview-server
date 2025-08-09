package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.RoleService;
import com.mock.interview.auth.application.port.in.SecurityService;
import com.mock.interview.auth.application.port.in.UserService;
import com.mock.interview.auth.application.port.out.UserOAuthProviderRepository;
import com.mock.interview.lib.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final UserOAuthProviderRepository userOAuthProviderRepository;
    private final RoleService roleService;

    @Override
    public UserModel findCurrentUser() {
        log.debug("Attempting to find Current User");

        var user = userService.findById((Long) SecurityContextHolder.getContext().getAuthentication().getDetails());

        log.debug("Current User is Found");
        return user;
    }

    @Override
    public UserModel authenticate(Authentication authentication) {
        log.debug("Attempting to authenticate User");

        var userData = authentication.getPrincipal();

        log.debug("{}", userData);
        return null;
    }
}
