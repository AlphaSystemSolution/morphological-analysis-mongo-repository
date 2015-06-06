package com.alphasystem.morphologicalanalysis.spring.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author sali
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.alphasystem.morphologicalanalysis.wordbyword.repository"},
        mongoTemplateRef = "wordByWordTemplate")
public class WordByWordConfig {
}
