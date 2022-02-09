package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.commons.dto.LeaveEventDto;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface LeaveMapper {
	
	@Mapping(source = "responsabile", target = "responsabile")
	LeaveEventDto mapToDto(LeaveEvent source);
	
	LeaveEvent mapToEntity(LeaveEventDto source);
	
	List<LeaveEventDto> mapToDtoList(List<LeaveEvent> source);
}
