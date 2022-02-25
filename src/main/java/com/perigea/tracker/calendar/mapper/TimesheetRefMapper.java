package com.perigea.tracker.calendar.mapper;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.TimesheetReferences;
import com.perigea.tracker.commons.dto.TimesheetRefDto;

@Mapper(componentModel = "spring")
public interface TimesheetRefMapper {

	TimesheetRefDto mapToDto(TimesheetReferences entity);

	TimesheetReferences mapToEntity(TimesheetRefDto dto);
	
	
}



