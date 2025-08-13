package com.mock.interview.auth.infrastructure.persistence.mapper;

import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserOAuthProviderEntityMapper extends EntityMappable<UserOAuthProviderEntity, UserOAuthProviderModel> {
    UserOAuthProviderEntityMapper INSTANCE = Mappers.getMapper(UserOAuthProviderEntityMapper.class);
}
