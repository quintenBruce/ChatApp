package com.qcbrucee.FirstApplication;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
class ApplicationConfiguration {

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}