package com.mock.interview.userProfile.infrastructure.persistence.mapper;

import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.userProfile.infrastructure.persistence.entity.InterviewMetadataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InterviewMetadataEntityMapper extends EntityMappable<InterviewMetadataEntity, InterviewMetadataModel> {
    InterviewMetadataEntityMapper INSTANCE = Mappers.getMapper(InterviewMetadataEntityMapper.class);
}
