package com.mock.interview.userProfile.application.port.in;

import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.lib.specification.SearchCriteria;
import com.mock.interview.userProfile.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserProfileService {
    List<UserProfileModel> findAll();

    UserProfileModel findById(Long id);

    UserProfileModel save(UserProfileModel userProfileModel);

    void delete(Long id);

    Page<UserProfileEntity> search(SearchCriteria searchCriteria);

    List<UserProfileEntity> searchAll(SearchCriteria searchCriteria);
}
