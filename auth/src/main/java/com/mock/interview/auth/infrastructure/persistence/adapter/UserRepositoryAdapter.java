package com.mock.interview.auth.infrastructure.persistence.adapter;

import com.mock.interview.auth.application.port.out.UserRepository;
import com.mock.interview.auth.infrastructure.persistence.mapper.UserEntityMapper;
import com.mock.interview.auth.infrastructure.persistence.repository.UserEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private static final UserEntityMapper userEntityMapper = UserEntityMapper.INSTANCE;

    private final UserEntityRepository userEntityRepository;

    @Override
    public UserModel findById(Long id) {
        return userEntityMapper.toModel(
            userEntityRepository.findById(id).orElseThrow(
                () -> {
                    var message = String.format("User with id %s not found", id);
                    log.info(message);
                    return new ResourceNotFoundException(message);
                }
            )
        );
    }

    @Override
    public UserModel findByLogin(String login) {
        return userEntityMapper.toModel(
            userEntityRepository.findByLogin(login).orElseThrow(
                () -> {
                    var message = String.format("User with email %s not found", login);
                    log.info(message);
                    return new ResourceNotFoundException(message);
                }
            )
        );
    }

    @Override
    public UserModel save(UserModel user) {
        return userEntityMapper.toModel(
            userEntityRepository.save(
                userEntityMapper.toEntity(user)
            )
        );
    }

    @Override
    public void delete(UserModel userModel) {
        userEntityRepository.deleteById(userModel.getId());
    }

    @Override
    public boolean existsByLogin(String login) {
        return userEntityRepository.existsByLogin(login);
    }
}
