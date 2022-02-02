package com.perigea.tracker.calendar.configuration;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/** questa classe config contiene la configurazione del database Mongo
 * */
@Configuration
@EnableMongoRepositories(basePackages = "com.perigea.tracker.calendar.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {
	
	@Value("${mongo.database_name}")
	private String databaseName;
	
	// TODO cambiare il nome! attualmente è "test"
	@Value("${mongo.address}")
	private String mongoAddress;
 
    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
 
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoAddress + databaseName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        
        return MongoClients.create(mongoClientSettings);
    }
 
    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.perigea");
    }
}