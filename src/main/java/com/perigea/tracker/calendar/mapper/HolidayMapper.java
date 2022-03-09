package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.model.HolidayEvent;
import com.perigea.tracker.commons.dto.HolidayEventDto;

@Mapper(componentModel = "spring")
public interface HolidayMapper {

	HolidayEventDto mapToDto(HolidayEvent source);

	HolidayEvent mapToEntity(HolidayEventDto source);

	List<HolidayEventDto> mapToDtoList(List<HolidayEvent> source);

	List<HolidayEvent> mapToEntityList(List<HolidayEventDto> source);
}
