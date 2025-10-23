package com.mock.interview.mapper;

import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.entity.UserEntity;
import com.mock.interview.lib.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserModelMapper extends EntityMappable<UserEntity, UserModel> {
    UserModelMapper INSTANCE = Mappers.getMapper(UserModelMapper.class);
}
