package com.mock.interview.auth2.repository;

import com.mock.interview.lib.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
