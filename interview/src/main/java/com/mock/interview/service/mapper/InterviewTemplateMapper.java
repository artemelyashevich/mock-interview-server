package com.mock.interview.service.mapper;

import com.mock.interview.entity.InterviewTemplateEntity;
import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.InterviewTemplateModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewTemplateMapper extends EntityMappable<InterviewTemplateEntity, InterviewTemplateModel> {
    InterviewTemplateMapper INSTANCE = Mappers.getMapper(InterviewTemplateMapper.class);
}
