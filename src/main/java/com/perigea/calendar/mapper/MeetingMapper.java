package com.perigea.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.perigea.calendar.dto.MeetingEventDto;
import com.perigea.calendar.entity.MeetingEvent;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface MeetingMapper {
	
	MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);
	
	MeetingEventDto mapToDto(MeetingEvent source);
	
	
	MeetingEvent mapToEntity(MeetingEventDto source);
	
	List<MeetingEventDto> mapToDtoList(List<MeetingEvent> source);
}
