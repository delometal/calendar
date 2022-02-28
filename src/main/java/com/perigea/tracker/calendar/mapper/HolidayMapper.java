package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.HolidayEvent;
import com.perigea.tracker.commons.dto.HolidayEventDto;

@Mapper(componentModel = "spring", uses = { ContactMapper.class, CalendarEventMapper.class })
public interface HolidayMapper {

	HolidayEventDto mapToDto(HolidayEvent source);

	HolidayEvent mapToEntity(HolidayEventDto source);

	@IterableMapping(elementTargetType = HolidayEventDto.class)
	List<HolidayEventDto> mapToDtoList(List<HolidayEvent> source);
}
