package com.mock.interview.service.mapper;

import com.mock.interview.entity.InterviewQuestionEntity;
import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.InterviewQuestionModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewQuestionMapper extends EntityMappable<InterviewQuestionEntity, InterviewQuestionModel> {
    InterviewQuestionMapper INSTANCE = Mappers.getMapper(InterviewQuestionMapper.class);
}