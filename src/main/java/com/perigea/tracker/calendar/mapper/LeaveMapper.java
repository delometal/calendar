package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.dto.LeaveEventDto;
import com.perigea.tracker.calendar.entity.LeaveEvent;

@Mapper(componentModel = "spring")
public interface LeaveMapper {
		
	LeaveEventDto mapToDto(LeaveEvent source);
	
	LeaveEvent mapToEntity(LeaveEventDto source);
	
	List<LeaveEventDto> mapToDtoList(List<LeaveEvent> source);
}
