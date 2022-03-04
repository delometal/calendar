package com.perigea.tracker.calendar.mapper;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.ScheduledEvent;
import com.perigea.tracker.commons.dto.ScheduledEventDto;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

	ScheduledEventDto mapToDto(ScheduledEvent entity);
	
	ScheduledEvent mapToEntity(ScheduledEventDto dto);

}
