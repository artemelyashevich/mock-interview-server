package com.mock.interview.userProfile.application.port.out;

import com.mock.interview.lib.model.UserProfileModel;

import java.util.List;

public interface UserProfileRepository {

    List<UserProfileModel> findAll();

    UserProfileModel findById(Long id);

    UserProfileModel save(UserProfileModel userProfileModel);

    void delete(UserProfileModel userProfileModel);
}
