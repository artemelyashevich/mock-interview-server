package com.mock.interview.infrastructure.web.mapper;

import com.mock.interview.lib.contract.DtoMappable;
import com.mock.interview.lib.dto.CreateInterviewRequest;
import com.mock.interview.lib.model.InterviewModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewDtoMapper extends DtoMappable<CreateInterviewRequest, InterviewModel> {
    InterviewDtoMapper INSTANCE = Mappers.getMapper(InterviewDtoMapper.class);
}
