package com.perigea.tracker.calendar.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perigea.tracker.calendar.entity.ArchivedEvent;

public interface ArchivedEventRepository extends MongoRepository<ArchivedEvent, String>{

}
