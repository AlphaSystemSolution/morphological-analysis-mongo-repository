/**
 * 
 */
package com.alphasystem.morphologicalanalysis.spring.support;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.alphasystem.persistence.mongo.spring.support.config.MongoConfig;

/**
 * @author sali
 * 
 */
@Configuration
@ComponentScan(basePackages = { "com.alphasystem.morphologicalanalysis.jquran",
		"com.alphasystem.morphologicalanalysis.util",
		"com.alphasystem.morphologicalanalysis.repository.listener" })
@EnableMongoRepositories(basePackages = { "com.alphasystem.morphologicalanalysis.repository" })
public class MorphologicalAnalysisSpringConfiguration extends MongoConfig {

}
