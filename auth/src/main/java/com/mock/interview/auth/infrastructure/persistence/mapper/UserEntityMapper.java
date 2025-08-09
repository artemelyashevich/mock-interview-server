package com.mock.interview.auth.infrastructure.persistence.mapper;

import com.mock.interview.auth.infrastructure.persistence.entity.UserEntity;
import com.mock.interview.lib.mapper.Mappable;
import com.mock.interview.lib.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper extends Mappable<UserEntity, UserModel> {
  UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);
}
