package com.alphasystem.morphologicalanalysis.spring.support;

import com.alphasystem.morphologicalanalysis.util.Script;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfig.class);
    private static final String SCRIPT_TYPE_PROPERTY = "script";
    public static final String MONGO_DB_NAME_PROPERTY = "mongo.db.name";
    private static final String MONGO_HOST_NAME_PROPERTY = "mongo.host.url";
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final Script DEFAULT_SCRIPT = Script.SIMPLE_ENHANCED;

    public static Script getCurrentScript(){
        Script script = DEFAULT_SCRIPT;
        String scriptName = getProperty(SCRIPT_TYPE_PROPERTY);
        try {
            if (scriptName != null) {
                script = Script.valueOf(scriptName);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Illegal value \"{}\" for script, defaulting to \"SIMPLE_ENHANCED\" script", scriptName);
            script = DEFAULT_SCRIPT;
        }
        return script;
    }

    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient(getProperty(MONGO_HOST_NAME_PROPERTY, LOCAL_HOST));
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        Script script = getCurrentScript();
        LOGGER.info("Connecting to database: {}", script.getDbName());
        return new SimpleMongoDbFactory(this.mongoClient(), script.getDbName());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public DbRefResolver dbRefResolver() throws Exception {
        return new DefaultDbRefResolver(mongoDbFactory());
    }

    @Bean
    public MappingContext mappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public MongoConverter mongoConverter() throws Exception {
        return mongoTemplate().getConverter();
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mongoConverter());
    }

}
