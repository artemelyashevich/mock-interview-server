package com.mock.interview.userProfile.application.service;

import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.userProfile.application.port.in.UserProfileService;
import com.mock.interview.userProfile.application.port.out.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfileModel> findAll() {
        log.debug("Find all user profiles");

        var users = userProfileRepository.findAll();

        log.debug("Found {} user profiles", users.size());
        return users;
    }

    @Override
    public UserProfileModel findById(Long id) {
        log.debug("Find user profile by id {}", id);

        var profile = userProfileRepository.findById(id);

        log.debug("Found user profile by id {}", profile.getId());
        return profile;
    }

    @Override
    public UserProfileModel save(UserProfileModel userProfileModel) {
        log.debug("Save user profile {}", userProfileModel);

        var profile = userProfileRepository.save(userProfileModel);

        log.debug("Saved user profile {}", profile.getId());
        return profile;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Delete user profile {}", id);

        var profile = findById(id);

        userProfileRepository.delete(profile);

        log.debug("Deleted user profile {}", id);
    }
}
