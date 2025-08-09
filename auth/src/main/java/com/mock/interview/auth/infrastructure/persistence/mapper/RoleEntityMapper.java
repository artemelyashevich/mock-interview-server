package com.mock.interview.auth.infrastructure.persistence.mapper;

import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import com.mock.interview.lib.mapper.Mappable;
import com.mock.interview.lib.model.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleEntityMapper extends Mappable<RoleEntity, RoleModel> {
  RoleEntityMapper INSTANCE = Mappers.getMapper(RoleEntityMapper.class);
}