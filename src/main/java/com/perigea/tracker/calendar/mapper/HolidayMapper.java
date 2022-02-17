package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.perigea.tracker.calendar.entity.HolidayEvent;
import com.perigea.tracker.commons.dto.HolidayEventDto;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface HolidayMapper {
	
	@Mapping(source = "responsabile", target = "responsabile")
	HolidayEventDto mapToDto(HolidayEvent source);
	
	HolidayEvent mapToEntity(HolidayEventDto source);
	
	List<HolidayEventDto> mapToDtoList(List<HolidayEvent> source);
}
