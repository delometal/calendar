package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.EventContact;
import com.perigea.tracker.commons.dto.EventContactDto;

@Mapper(componentModel = "spring")
public interface ContactMapper {
	
	EventContactDto mapToDto(EventContact source);
	
	EventContact mapToEntity(EventContactDto source);
	
	List<EventContactDto> mapToDtoList(List<EventContact> source);
	
	List<EventContact> mapToEntityList(List<EventContactDto> source);
}
