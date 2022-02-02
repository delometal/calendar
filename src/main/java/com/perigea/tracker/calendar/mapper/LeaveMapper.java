package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.LeaveEvent;
import com.perigea.tracker.commons.dto.LeaveEventDto;

@Mapper(componentModel = "spring")
public interface LeaveMapper {
		
	LeaveEventDto mapToDto(LeaveEvent source);
	
	LeaveEvent mapToEntity(LeaveEventDto source);
	
	List<LeaveEventDto> mapToDtoList(List<LeaveEvent> source);
}
