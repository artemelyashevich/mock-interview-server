package com.mock.interview.infrastructure.persistence.mapper;

import com.mock.interview.infrastructure.persistence.entity.InterviewTemplateEntity;
import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.InterviewTemplateModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewTemplateEntityMapper extends EntityMappable<InterviewTemplateEntity, InterviewTemplateModel> {
    InterviewTemplateEntityMapper INSTANCE = Mappers.getMapper(InterviewTemplateEntityMapper.class);
}
