package com.perigea.tracker.calendar.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.perigea.tracker.calendar.entity.Contact;
import com.perigea.tracker.commons.dto.EventContactDto;

@Mapper(componentModel = "spring")
public interface ContactMapper {
	
	EventContactDto mapToDto(Contact source);
	
	Contact mapToEntity(EventContactDto source);
	
	List<EventContactDto> mapToDtoList(List<Contact> source);
	
	List<Contact> mapToEntityList(List<EventContactDto> source);
}
