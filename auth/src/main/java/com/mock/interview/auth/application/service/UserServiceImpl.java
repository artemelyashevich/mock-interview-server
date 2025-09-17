package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.UserService;
import com.mock.interview.auth.application.port.out.UserRepository;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserModel findById(@NonNull Long id) {
        log.debug("Attempting find user by id: {}", id);

        var user = userRepository.findById(id);

        log.debug("User found by id: {}", user.getId());
        return user;
    }

    @Override
    public UserModel findByLogin(@NonNull String login) {
        log.debug("Attempting find user by login: {}", login);

        var user = userRepository.findByLogin(login);

        log.debug("User found by login: {}", user.getLogin());
        return user;
    }

    @Override
    public UserModel save(@NonNull UserModel user) {
        log.debug("Attempting to save user: {}", user);

        if (userRepository.existsByLogin(user.getLogin())) {
            var message = String.format("User with login %s already exists", user.getLogin());
            log.debug(message);
            throw new ResourceAlreadyExistException(message);
        }

        var newUser = userRepository.save(user);

        log.debug("User saved: {}", newUser);
        return newUser;
    }

    @Override
    public void delete(@NonNull UserModel userModel) {
        log.debug("Attempting to delete user: {}", userModel);

        if (!userRepository.existsByLogin(userModel.getLogin())) {
            var message = String.format("User with login %s does not exist", userModel.getLogin());
            log.debug(message);
            throw new ResourceNotFoundException(message);
        }

        userRepository.delete(userModel);
        log.debug("User deleted: {}", userModel);
    }

    @Override
    @Transactional
    public UserModel update(@NonNull UserModel user) {
        log.debug("Attempting to update user: {}", user);

        var oldUser = userRepository.findById(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setOAuthProviderModelList(user.getOAuthProviderModelList());

        var newUser = userRepository.save(oldUser);

        log.debug("User updated: {}", oldUser);
        return newUser;
    }
}
