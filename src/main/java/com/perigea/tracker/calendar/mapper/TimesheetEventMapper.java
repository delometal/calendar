package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.entity.TimesheetEvent;
import com.perigea.tracker.commons.dto.TimesheetEventDto;

@Mapper(componentModel = "spring", uses = { TimesheetRefMapper.class, ContactMapper.class })
public interface TimesheetEventMapper {

	TimesheetEventDto mapToDto(TimesheetEvent entity);

	TimesheetEvent mapToEntity(TimesheetEventDto dto);

	List<TimesheetEventDto> mapToDtoList(List<TimesheetEvent> entities);

	List<TimesheetEvent> mapToEntityList(List<TimesheetEventDto> dtos);
}
