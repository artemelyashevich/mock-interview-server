package com.mock.interview.report.mapper;

import com.mock.interview.lib.contract.DtoMappable;
import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportDtoMapper extends DtoMappable<CreateReportRequest, ReportModel> {
    ReportDtoMapper INSTANCE = Mappers.getMapper(ReportDtoMapper.class);
}
