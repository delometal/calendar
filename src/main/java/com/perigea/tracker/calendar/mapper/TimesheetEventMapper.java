package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.perigea.tracker.calendar.entity.TimesheetEvent;
import com.perigea.tracker.commons.dto.TimesheetEventDto;

@Mapper(componentModel = "spring", uses = { CalendarEventMapper.class, ContactMapper.class, TimesheetRefMapper.class })
public interface TimesheetEventMapper {
	
	
	TimesheetEventDto mapToDto(TimesheetEvent entity);
	

	TimesheetEvent mapToEntity(TimesheetEventDto dto);

	@IterableMapping(elementTargetType = TimesheetEventDto.class)
	List<TimesheetEventDto> mapToDtoList(List<TimesheetEvent> entities);

}
