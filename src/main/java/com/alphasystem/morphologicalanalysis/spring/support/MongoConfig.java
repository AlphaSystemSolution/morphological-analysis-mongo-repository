package com.alphasystem.morphologicalanalysis.spring.support;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

import static java.lang.System.getProperty;

/**
 * @author sali
 */
@Configuration
public class MongoConfig {

    public static final String GRAPH_DB_NAME_PROPERTY = "graph.db.name";
    public static final String MONGO_DB_NAME_PROPERTY = "mongo.db.name";
    public static final String MONGO_HOST_NAME_PROPERTY = "mongo.host.url";
    private static final String LOCAL_HOST = "127.0.0.1";

    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient(getProperty(MONGO_HOST_NAME_PROPERTY, LOCAL_HOST));
    }

    @Bean
    public MongoDbFactory mongoGraphDbFactory() throws Exception {
        return new SimpleMongoDbFactory(this.mongoClient(),
                getProperty(GRAPH_DB_NAME_PROPERTY, "__DEFAULT__"));
    }

    @Bean
    public MongoDbFactory mongoWordByWordDbFactory() throws Exception {
        return new SimpleMongoDbFactory(this.mongoClient(),
                getProperty(MONGO_DB_NAME_PROPERTY, "__DEFAULT__"));
    }

    @Bean(name = "graphTemplate")
    public MongoTemplate graphTemplate() throws Exception {
        return new MongoTemplate(mongoGraphDbFactory());
    }

    @Bean(name = "wordByWordTemplate")
    public MongoTemplate wordByWordTemplate() throws Exception {
        return new MongoTemplate(mongoWordByWordDbFactory());
    }
}
