package com.alphasystem.morphologicalanalysis.spring.support;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sali
 */
@Configuration
@ComponentScan(basePackages = {"com.alphasystem.persistence.mongo.spring.support",
        "com.alphasystem.morphologicalanalysis.util",
        "com.alphasystem.morphologicalanalysis.wordbyword.listener",
        "com.alphasystem.morphologicalanalysis.graph.listener",
        "com.alphasystem.morphologicalanalysis.morphology.listener",
        "com.alphasystem.morphologicalanalysis.morphology.repository"})
public class MorphologicalAnalysisSpringConfiguration {

}
