package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.repository.MorphologicalEntryRepository;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.QLocation;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.QRootWord;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RootWord;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.mysema.query.types.expr.BooleanExpression;
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

    @Test(dependsOnMethods = {"getDependencyGraphs"})
    public void printLocationsWithRootOrFormPopulated() {
        QLocation qLocation = QLocation.location;
        QRootWord qRootWord = qLocation.rootWord;
        BooleanExpression predicate = qRootWord.firstRadical.isNotNull().or(qRootWord.secondRadical.isNotNull())
                .or(qRootWord.thirdRadical.isNotNull()).or(qRootWord.fourthRadical.isNotNull());
        predicate = predicate.or(qLocation.formTemplate.isNotNull());
        log(format("Query {%s}", predicate), true);
        LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        List<Location> locations = (List<Location>) locationRepository.findAll(predicate);
        if (locations == null || locations.isEmpty()) {
            log("No Location with root word or form populated exists", true);
        } else {
            locations.forEach(location -> {
                RootWord rootWord = location.getRootWord();
                String id = rootWord.getId();
                String displayName = rootWord.toCode();
                loadMorphologicalEntry(location);
                log(format("Location: %s, Root: %s (%s), Form: %s", location, id, displayName,
                        location.getFormTemplate()), true);
                repositoryUtil.getLocationRepository().save(location);
            });
            log(format("Total number of locations: %s", locations.size()), true);
        }
    }

    @Test(dependsOnMethods = {"printLocationsWithRootOrFormPopulated"})
    public void printLocationsWithMorphologicalEntriesPopulated() {
        MorphologicalEntryRepository morphologicalEntryRepository = repositoryUtil.getMorphologicalEntryRepository();
        List<MorphologicalEntry> morphologicalEntries = (List<MorphologicalEntry>) morphologicalEntryRepository.findAll();
        if (morphologicalEntries == null || morphologicalEntries.isEmpty()) {
            log("No MorphologicalEntries exists", true);
        } else {
            morphologicalEntries.forEach(entry -> log(format("Morphological Entry {%s}", entry), true));
        }
    }

    //@Test(dependsOnMethods = {"getDependencyGraphs"})
    public void printLocationsWithRootNotPopulated() {
        QLocation qLocation = QLocation.location;
        QRootWord qRootWord = qLocation.rootWord;
        BooleanExpression predicate = qRootWord.isNotNull().and(qRootWord.firstRadical.isNull().and(qRootWord.secondRadical.isNull())
                .and(qRootWord.thirdRadical.isNull()).and(qRootWord.fourthRadical.isNull()));
        log(format("Query {%s}", predicate), true);
        LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        List<Location> locations = (List<Location>) locationRepository.findAll(predicate);
        if (locations == null || locations.isEmpty()) {
            log("No Location with root word null exists", true);
        } else {
            locations.forEach(location -> {
                RootWord rootWord = location.getRootWord();
                String id = (rootWord == null) ? null : rootWord.getId();
                String displayName = (rootWord == null) ? null : rootWord.toCode();
                log(format("Location: %s, Root: %s (%s)", location, id, displayName), true);
                // location.setRootWord(null);
            });
            // locationRepository.save(locations);
            log(format("Total number of locations: %s", locations.size()), true);
        }
    }

    private void loadMorphologicalEntry(Location location) {
        MorphologicalEntry morphologicalEntry = new MorphologicalEntry();
        RootWord rootWord = location.getRootWord();
        RootLetters rootLetters = null;
        if (rootWord != null && rootWord.getFirstRadical() != null && rootWord.getSecondRadical()
                != null && rootWord.getThirdRadical() != null) {
            rootLetters = new RootLetters(rootWord.getFirstRadical(), rootWord.getSecondRadical(),
                    rootWord.getThirdRadical());
        }
        log(format("Location: {%s}, RL: {%s}, RW: {%s}", location, rootLetters, rootWord.toCode()), true);
        morphologicalEntry.setRootLetters(rootLetters);
        morphologicalEntry.setForm(location.getFormTemplate());
        repositoryUtil.saveMorphologicalEntry(morphologicalEntry, location);
    }
}
