package com.mock.interview.userProfile.infrastructure.web.mapper;

import com.mock.interview.lib.contract.DtoMappable;
import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.lib.dto.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileDtoMapper extends DtoMappable<UserProfileDto, UserProfileModel> {
    UserProfileDtoMapper INSTANCE = Mappers.getMapper(UserProfileDtoMapper.class);
}
