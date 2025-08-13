package com.mock.interview.userProfile.infrastructure.persistence.mapper;

import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.userProfile.infrastructure.persistence.entity.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileEntityMapper extends EntityMappable<UserProfileEntity, UserProfileModel> {
    UserProfileEntityMapper INSTANCE = Mappers.getMapper(UserProfileEntityMapper.class);
}
