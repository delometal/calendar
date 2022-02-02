package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.perigea.tracker.calendar.entity.MeetingEvent;
import com.perigea.tracker.commons.dto.MeetingEventDto;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface MeetingMapper {
	
	MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);
	
	MeetingEventDto mapToDto(MeetingEvent source);
	
	MeetingEvent mapToEntity(MeetingEventDto source);
	
	List<MeetingEventDto> mapToDtoList(List<MeetingEvent> source);
}
