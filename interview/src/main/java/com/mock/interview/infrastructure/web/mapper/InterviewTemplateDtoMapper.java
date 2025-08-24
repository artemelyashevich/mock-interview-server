package com.mock.interview.infrastructure.web.mapper;

import com.mock.interview.lib.contract.DtoMappable;
import com.mock.interview.lib.dto.InterviewTemplateRequest;
import com.mock.interview.lib.model.InterviewTemplateModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewTemplateDtoMapper extends DtoMappable<InterviewTemplateRequest, InterviewTemplateModel> {

    InterviewTemplateDtoMapper INSTANCE = Mappers.getMapper(InterviewTemplateDtoMapper.class);
}
