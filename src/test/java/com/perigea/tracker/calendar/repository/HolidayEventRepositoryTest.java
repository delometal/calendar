package com.perigea.tracker.calendar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import com.perigea.tracker.calendar.configuration.MongoConfig;
import com.perigea.tracker.calendar.entity.HolidayRequestEvent;
import com.perigea.tracker.calendar.model.Contact;

@DataMongoTest
@ContextConfiguration(classes = {MongoConfig.class})
class HolidayEventRepositoryTest {
	
	@Autowired 
	private HolidayEventRepository repository;
	private static HolidayRequestEvent firstEvent;
	private static HolidayRequestEvent secondEvent;
	private static Contact responsabile;
	private static Contact creator;
	private static final Integer TOTAL = 2;
	
	@BeforeAll
	public static void dataSetup(@Autowired HolidayEventRepository repository) {
		creator = new Contact();
		firstEvent = new HolidayRequestEvent();
		secondEvent = new HolidayRequestEvent();
		responsabile = new Contact();
		
		creator.setMailAziendale("pluto@perigea.it");
		responsabile.setMailAziendale("capo@perigea.it");
		
		firstEvent.setId("unique");
		firstEvent.setResponsabile(responsabile);
		firstEvent.setEventCreator(creator);
		
		secondEvent.setId("second");
		secondEvent.setResponsabile(responsabile);
		
		repository.save(firstEvent);
		repository.save(secondEvent);
	}
	
	@Test
	public void findByIdTest() {
		String id = "unique";
		HolidayRequestEvent result = repository.findById(id).get();
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(firstEvent);
	}
	
	@Test
	public void findAllTest() {
		List<HolidayRequestEvent> all = repository.findAll();
		assertThat(all.size()).isEqualTo(TOTAL);
	}
	
	@Test
	public void findByCreaetorTest() {
		String creator_ = creator.getMailAziendale();
		List<HolidayRequestEvent> result = repository.findAllByEventCreator(creator_);
		assertThat(result.get(0)).isNotNull();
		assertThat(result.get(0)).isEqualTo(firstEvent);
	}
	
	@Test
	public void deleteTest() {
		repository.delete(firstEvent);
		List<HolidayRequestEvent> all = repository.findAll();
		assertThat(all.size()).isEqualTo(TOTAL - 1);

	}

}
