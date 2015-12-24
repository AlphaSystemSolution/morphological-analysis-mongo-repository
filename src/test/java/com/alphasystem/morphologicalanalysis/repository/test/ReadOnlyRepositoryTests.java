package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.repository.MorphologicalEntryRepository;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.testng.Assert.*;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class ReadOnlyRepositoryTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @BeforeSuite
    public void beforeSuite() {
        log("Checking database", true);
        String dbName = getProperty(MongoConfig.MONGO_DB_NAME_PROPERTY);
        log(format("Database in use {%s}", dbName), true);
        assertTrue("MORPHOLOGICAL_ANALYSIS_DB".equals(dbName));
    }

    @Test
    public void getTokenByDisplayName() {
        Token token = new Token(1, 1, 1, "");
        token.initDisplayName();
        String displayName = token.getDisplayName();
        token = repositoryUtil.getTokenRepository().findByDisplayName(displayName);
        assertNotNull(token);
        log(format("Token Display Name: %s", token.getDisplayName()), true);
        assertEquals(token.getDisplayName(), displayName);
    }

    @Test(dependsOnMethods = "getTokenByDisplayName")
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

    //@Test(dependsOnMethods = {"getTokens"})
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

    //@Test(dependsOnMethods = {"getTokens"})
    public void printLocationsWithMorphologicalEntriesPopulated() {
        MorphologicalEntryRepository morphologicalEntryRepository = repositoryUtil.getMorphologicalEntryRepository();
        List<MorphologicalEntry> morphologicalEntries = (List<MorphologicalEntry>) morphologicalEntryRepository.findAll();
        if (morphologicalEntries == null || morphologicalEntries.isEmpty()) {
            log("No MorphologicalEntries exists", true);
        } else {
            morphologicalEntries.forEach(entry -> log(format("Morphological Entry {%s}", entry), true));
        }
    }

    // @Test(dependsOnMethods = {"printLocationsWithMorphologicalEntriesPopulated"})
    public void printLocationsWithRootNotPopulated() {
        LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        List<Location> locations = (List<Location>) locationRepository.findAll();
        if (locations == null || locations.isEmpty()) {
            log("No Location with root word null exists", true);
        } else {
            locations.forEach(location -> {
                locationRepository.save(location);
                log(format("Saved: {%s}", location));
            });
            log(format("Total number of locations: %s", locations.size()), true);
        }
    }

    @Test(dependsOnMethods = "getTokens")
    public void testNextToken() {
        // boundary case
        TokenRepository tokenRepository = repositoryUtil.getTokenRepository();
        String displayName = "114:6:3";
        log(format("Boundary Case, getNextToken: {%s}", displayName), true);
        Token token = tokenRepository.findByDisplayName(displayName);
        assertNotNull(token);
        Token nextToken = repositoryUtil.getNextToken(token);
        assertNull(nextToken);
        log(format("No next token found", displayName), true);
    }

}
