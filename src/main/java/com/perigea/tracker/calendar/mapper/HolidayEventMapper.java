package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.HolidayRequestEvent;
import com.perigea.tracker.commons.dto.HolidayEventRequestDto;

@Mapper(componentModel = "spring", uses = { ContactMapper.class, CalendarEventMapper.class, HolidayMapper.class })
public interface HolidayEventMapper {

	HolidayEventRequestDto mapToDto(HolidayRequestEvent source);

	HolidayRequestEvent mapToEntity(HolidayEventRequestDto source);

	@IterableMapping(elementTargetType = HolidayEventRequestDto.class)
	List<HolidayEventRequestDto> mapToDtoList(List<HolidayRequestEvent> source);
}
