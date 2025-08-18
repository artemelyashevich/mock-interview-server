package com.elyashevich.interview.infrastructure.web.mapper;

import com.elyashevich.interview.infrastructure.web.dto.InterviewQuestionRequest;
import com.mock.interview.lib.contract.DtoMappable;
import com.mock.interview.lib.model.InterviewModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewQuestionDtoMapper extends DtoMappable<InterviewQuestionRequest, InterviewModel> {

    InterviewQuestionDtoMapper INSTANCE = Mappers.getMapper(InterviewQuestionDtoMapper.class);
}
