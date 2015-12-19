package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class ReadOnlyRepositoryTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @Test
    public void getTokens() {
        VerseTokenPairGroup group = new VerseTokenPairGroup();
        group.setChapterNumber(2);
        group.setPairs(new VerseTokensPair(25, 30, 34), new VerseTokensPair(26, 1, 5));

        List<Token> tokens = repositoryUtil.getTokens(group);
        assertNotNull(tokens);
        assertEquals(tokens.isEmpty(), false);
        assertEquals(tokens.size(), 10);
        tokens.forEach(token -> log(format("Token: {%s}", token), true));
    }

    @Test(dependsOnMethods = {"getTokens"})
    public void getDependencyGraphs() {
        VerseTokenPairGroup group = new VerseTokenPairGroup();
        group.setChapterNumber(18);
        group.setPairs(new VerseTokensPair(1, 1, 7), new VerseTokensPair(1, 8, 11));
        List<DependencyGraph> dependencyGraphs = repositoryUtil.getDependencyGraphs(group);
        assertNotNull(dependencyGraphs);
        assertEquals(dependencyGraphs.isEmpty(), false);
        assertEquals(dependencyGraphs.size(), 2);
        dependencyGraphs.forEach(dependencyGraph -> log(format("Dependency Graph: {%s}", dependencyGraph), true));
    }
}
