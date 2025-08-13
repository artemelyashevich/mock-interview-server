package com.mock.interview.userProfile.application.port.in;

import com.mock.interview.lib.model.UserProfileModel;

import java.util.List;

public interface UserProfileService {
    List<UserProfileModel> findAll();

    UserProfileModel findById(Long id);

    UserProfileModel save(UserProfileModel userProfileModel);

    void delete(Long id);
}
