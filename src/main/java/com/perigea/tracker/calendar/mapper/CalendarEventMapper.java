package com.perigea.tracker.calendar.mapper;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.CalendarEvent;
import com.perigea.tracker.commons.dto.CalendarEventDto;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface CalendarEventMapper {
	
	
	CalendarEventDto mapToDto(CalendarEvent source);
	
	
	CalendarEvent mapToEntity(CalendarEventDto source);
}
