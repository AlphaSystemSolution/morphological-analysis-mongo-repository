package com.alphasystem.morphologicalanalysis.spring.support;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

import static java.lang.System.getProperty;

/**
 * @author sali
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.alphasystem.morphologicalanalysis.wordbyword.repository",
        "com.alphasystem.morphologicalanalysis.graph.repository",
        "com.alphasystem.morphologicalanalysis.morphology.repository"})
public class MongoConfig {

    public static final String MONGO_DB_NAME_PROPERTY = "mongo.db.name";
    public static final String MONGO_HOST_NAME_PROPERTY = "mongo.host.url";
    private static final String LOCAL_HOST = "127.0.0.1";

    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient(getProperty(MONGO_HOST_NAME_PROPERTY, LOCAL_HOST));
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(this.mongoClient(),
                getProperty(MONGO_DB_NAME_PROPERTY, "__DEFAULT__"));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    /*@Bean
    public Morphia morphia(){
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.alphasystem.persistence.model");
        morphia.mapPackage("com.alphasystem.morphologicalanalysis.wordbyword.model");
        morphia.mapPackage("com.alphasystem.morphologicalanalysis.morphology.model");
        morphia.mapPackage("com.alphasystem.morphologicalanalysis.graph.model");
        return morphia;
    }*/

    /*@Bean
    public Datastore datastore() throws Exception{
        Datastore datastore = morphia().createDatastore(mongoClient(),
                getProperty(MONGO_DB_NAME_PROPERTY, "__DEFAULT__"));
        datastore.ensureCaps();
        datastore.ensureIndexes();
        return datastore;
    }*/
}
