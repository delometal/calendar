package com.perigea.calendar.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.perigea.calendar.dto.ContactDto;
import com.perigea.calendar.entity.Contact;
@Mapper(componentModel = "spring")
public interface ContactMapper {
	
	ContactDto mapToDto(Contact source);
	
	Contact mapToEntity(ContactDto source);
	
	List<ContactDto> mapToDtoList(List<Contact> source);
	
	List<Contact> mapToEntityList(List<ContactDto> source);
}
