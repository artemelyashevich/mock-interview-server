package com.mock.interview.mapper;

import com.mock.interview.entity.InterviewEntity;
import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.InterviewModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewEntityMapper extends EntityMappable<InterviewEntity, InterviewModel> {
    InterviewEntityMapper INSTANCE = Mappers.getMapper(InterviewEntityMapper.class);
}
