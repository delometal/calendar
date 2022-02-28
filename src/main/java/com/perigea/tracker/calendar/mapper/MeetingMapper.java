package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.commons.dto.MeetingEventDto;

@Mapper(componentModel = "spring", uses = { ContactMapper.class, CalendarEventMapper.class })
public interface MeetingMapper {

	MeetingEventDto mapToDto(MeetingEvent source);

	MeetingEvent mapToEntity(MeetingEventDto source);
	
	@IterableMapping(elementTargetType = MeetingEventDto.class)
	List<MeetingEventDto> mapToDtoList(List<MeetingEvent> source);
}
