package com.mock.interview.userProfile.infrastructure.persistence.adapter;

import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.userProfile.application.port.out.UserProfileRepository;
import com.mock.interview.userProfile.infrastructure.persistence.mapper.UserProfileEntityMapper;
import com.mock.interview.userProfile.infrastructure.persistence.repository.UserProfileEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepository {

    private static final UserProfileEntityMapper userProfileEntityMapper = UserProfileEntityMapper.INSTANCE;

    private final UserProfileEntityRepository userProfileEntityRepository;

    @Override
    public List<UserProfileModel> findAll() {
        return userProfileEntityMapper.toModels(userProfileEntityRepository.findAll());
    }

    @Override
    public UserProfileModel findById(Long id) {
        return userProfileEntityMapper.toModel(userProfileEntityRepository.findById(id)
            .orElseThrow(() -> {
                var message = String.format("UserProfile with id %s not found", id);
                log.debug(message);
                return new ResourceNotFoundException(message);
            }));
    }

    @Override
    public UserProfileModel save(UserProfileModel userProfileModel) {
        return userProfileEntityMapper.toModel(
            userProfileEntityRepository.save(
                userProfileEntityMapper.toEntity(userProfileModel)
            )
        );
    }

    @Override
    public void delete(UserProfileModel userProfileModel) {
        userProfileEntityRepository.deleteById(userProfileModel.getId());
    }
}
