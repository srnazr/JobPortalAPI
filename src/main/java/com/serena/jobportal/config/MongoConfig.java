package com.serena.jobportal.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        // This uses the same connection string as Spring Data MongoDB
        return MongoClients.create(connectionUri);
    }

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        // This codec registry enables automatic mapping between POJOs and MongoDB documents
        return fromRegistries(
                com.mongodb.MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient, CodecRegistry pojoCodecRegistry) {
        // Get a database instance with the POJO codec registered
        return mongoClient.getDatabase(databaseName)
                .withCodecRegistry(pojoCodecRegistry);
    }
}