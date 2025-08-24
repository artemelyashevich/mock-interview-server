package com.mock.interview.report.mapper;

import com.mock.interview.lib.contract.EntityMappable;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.report.entity.ReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportEntityMapper extends EntityMappable<ReportEntity, ReportModel> {
    ReportEntityMapper INSTANCE = Mappers.getMapper(ReportEntityMapper.class);
}
