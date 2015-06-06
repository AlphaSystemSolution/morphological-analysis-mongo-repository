package com.alphasystem.morphologicalanalysis.spring.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author sali
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.alphasystem.morphologicalanalysis.graph.repository"},
        mongoTemplateRef = "graphTemplate")
public class GraphConfig {
}
