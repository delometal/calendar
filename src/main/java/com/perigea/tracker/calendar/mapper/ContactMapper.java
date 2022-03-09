package com.perigea.tracker.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.tracker.calendar.model.Contact;
import com.perigea.tracker.commons.dto.ContactDto;

@Mapper(componentModel = "spring")
public interface ContactMapper {
	
	ContactDto mapToDto(Contact source);
	
	Contact mapToEntity(ContactDto source);
	
	List<ContactDto> mapToDtoList(List<Contact> source);
	
	List<Contact> mapToEntityList(List<ContactDto> source);
}
