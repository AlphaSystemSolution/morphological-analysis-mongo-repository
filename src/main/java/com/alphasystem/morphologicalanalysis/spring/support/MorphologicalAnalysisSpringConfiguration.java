/**
 * 
 */
package com.alphasystem.morphologicalanalysis.spring.support;

import com.alphasystem.persistence.mongo.spring.support.config.MongoConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author sali
 * 
 */
@Configuration
@ComponentScan(basePackages = { "com.alphasystem.morphologicalanalysis.jquran",
		"com.alphasystem.morphologicalanalysis.util",
		"com.alphasystem.morphologicalanalysis.wordbyword.repository.listener"})
@EnableMongoRepositories(basePackages = {"com.alphasystem.morphologicalanalysis.wordbyword.repository",
		"com.alphasystem.morphologicalanalysis.graph.repository"})
public class MorphologicalAnalysisSpringConfiguration extends MongoConfig {

}
