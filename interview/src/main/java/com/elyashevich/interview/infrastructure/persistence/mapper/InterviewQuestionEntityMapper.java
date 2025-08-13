package com.elyashevich.interview.infrastructure.persistence.mapper;

import com.elyashevich.interview.infrastructure.persistence.entity.InterviewQuestionEntity;
import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.InterviewQuestionModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewQuestionEntityMapper extends EntityMappable<InterviewQuestionEntity, InterviewQuestionModel> {
    InterviewQuestionEntityMapper INSTANCE = Mappers.getMapper(InterviewQuestionEntityMapper.class);
}
