package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.repository.MorphologicalEntryRepository;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.ChapterRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
        log(format("No next token found %s", displayName), true);
    }

    @Test
    public void testPage() {
        ChapterRepository chapterRepository = repositoryUtil.getChapterRepository();
        int pageSize = 10;
        Page<Chapter> page = chapterRepository.findAll(new PageRequest(0, pageSize));
        while (page.hasContent() || page.hasNext()) {
            log(format("Total Number Of Pages: %s, Current Page: %s, Total Number OfElements: %s", page.getTotalPages(),
                    page.getNumber(), page.getTotalElements()), true);
            List<Chapter> content = page.getContent();
            log("------------------------------------------------------------", true);
            content.forEach(chapter -> log(chapter.getDisplayName(), true));
            log("------------------------------------------------------------", true);
            log("", true);
            page = chapterRepository.findAll(new PageRequest(page.getNumber() + 1, pageSize));
        }

    }

    @SuppressWarnings("unused")
    private void populateTextInLocations() {
        final LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        int pageSize = 50;
        int pageNumber = 0;
        Page<Location> page = locationRepository.findAll(new PageRequest(pageNumber, pageSize));
        final int totalPages = page.getTotalPages();
        while (pageNumber <= totalPages) {
            final int currentPageNumber = page.getNumber();
            log(format("Total Number Of Pages: %s, Current Page: %s, Total Number Of Elements: %s", totalPages,
                    currentPageNumber, page.getTotalElements()), true);
            if (page.hasContent()) {
                final List<Location> locations = page.getContent();
                locations.forEach(location -> {
                    final String text = location.getText();
                    if (!location.isTransient() && StringUtils.isBlank(text)) {
                        final ArabicWord locationWord = repositoryUtil.getLocationWord(location);
                        System.out.println(format("Empty text for location \"%s\", Text would be \"%s\"", location, locationWord.toUnicode()));
                        location.setText(locationWord.toUnicode());
                        locationRepository.save(location);
                    }
                });
            }
            pageNumber = currentPageNumber + 1;
            page = locationRepository.findAll(new PageRequest(pageNumber, pageSize));
        }
    }

    @SuppressWarnings("unused")
    private void removeMorphologicalEntries() {
        final LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        final MorphologicalEntryRepository morphologicalEntryRepository = repositoryUtil.getMorphologicalEntryRepository();
        int pageSize = 50;
        int pageNumber = 0;
        Page<Location> page = locationRepository.findAll(new PageRequest(pageNumber, pageSize));
        final int totalPages = page.getTotalPages();
        while (pageNumber <= totalPages) {
            final int currentPageNumber = page.getNumber();
            log(format("Total Number Of Pages: %s, Current Page: %s, Total Number Of Elements: %s", totalPages,
                    currentPageNumber, page.getTotalElements()), true);
            if (page.hasContent()) {
                final List<Location> locations = page.getContent();
                locations.forEach(location -> {
                    final MorphologicalEntry morphologicalEntry = location.getMorphologicalEntry();
                    if (morphologicalEntry != null) {
                        System.out.println(format("Removing MorphologicalEntry \"%s-%s from location \"%s-%s\"",
                                morphologicalEntry.getDisplayName(), morphologicalEntry.getId(), location.getDisplayName(), location.getId()));
                        location.setMorphologicalEntry(null);
                        locationRepository.save(location);
                    }
                });
            }
            pageNumber = currentPageNumber + 1;
            page = locationRepository.findAll(new PageRequest(pageNumber, pageSize));
        }
    }

    @SuppressWarnings("unused")
    private void mergeTokens() {
        final int chapterNumber = 49;
        final int verseNumber = 2;
        final int[] tokenNumbers = {1, 2};
        repositoryUtil.mergeTokens(chapterNumber, verseNumber, tokenNumbers);
    }

}
