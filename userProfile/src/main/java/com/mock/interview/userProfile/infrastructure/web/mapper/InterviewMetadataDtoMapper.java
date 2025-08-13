package com.mock.interview.userProfile.infrastructure.web.mapper;

import com.mock.interview.lib.contract.DtoMappable;
import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.userProfile.infrastructure.web.dto.InterviewMetadataDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewMetadataDtoMapper extends DtoMappable<InterviewMetadataDto, InterviewMetadataModel> {
    InterviewMetadataDtoMapper INSTANCE = Mappers.getMapper(InterviewMetadataDtoMapper.class);
}
