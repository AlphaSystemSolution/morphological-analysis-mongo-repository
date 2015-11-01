package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.graph.repository.TerminalNodeRepository;
import com.alphasystem.morphologicalanalysis.spring.support.GraphConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.spring.support.WordByWordConfig;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.KanaAndSisters;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType.THIRD_PERSON;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType.MASCULINE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.KanaFamily.MEMBER1;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus.GENETIVE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounType.DEFINITE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType.PLURAL;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType.SINGULAR;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.DEFINITE_ARTICLE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.VERB;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbType.IMPERFECT;
import static java.lang.String.format;
import static java.util.Collections.reverse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
        MorphologicalAnalysisSpringConfiguration.class})
public class MorphologicalAnalysisTest extends AbstractTestNGSpringContextTests {

    private static final int DEFAULT_CHAPTER_NUMBER = 1;
    private static final int DEFAULT_VERSE_NUMBER = 2;

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @BeforeClass
    public void beforeSuite() {
        repositoryUtil.getMongoTemplate().getDb().dropDatabase();
    }

    @Test
    public void createChapter() {
        log(format("Creating chapter: %s", DEFAULT_CHAPTER_NUMBER), true);
        repositoryUtil.createChapter(DEFAULT_CHAPTER_NUMBER);
        log(format("Chapter %s created", DEFAULT_CHAPTER_NUMBER), true);

        Verse verse = repositoryUtil.getVerseRepository().
                findByChapterNumberAndVerseNumber(DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER);
        assertEquals(verse.getTokenCount(), new Integer(4));
    }

    @Test(dependsOnMethods = "createChapter")
    public void populateToken4() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 4);
        assertNotNull(token);
        int length = token.getTokenWord().getLength();
        log(format("Token word length: %s", length), true);
        Location location = token.getLocations().get(0);
        location.setPartOfSpeech(DEFINITE_ARTICLE);
        location.setStartIndex(0);
        location.setEndIndex(2);
        repositoryUtil.getLocationRepository().save(location);

        location = new Location(DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, token.getTokenNumber(), 2)
                .withStartIndex(2).withEndIndex(length);
        NounProperties properties = (NounProperties) location.getProperties();
        properties.setNounType(DEFINITE);
        properties.setGender(MASCULINE);
        properties.setNumber(PLURAL);
        properties.setStatus(GENETIVE);
        repositoryUtil.getLocationRepository().save(location);
        token.getLocations().add(location);
        repositoryUtil.getTokenRepository().save(token);
    }

    @Test(dependsOnMethods = "populateToken4")
    public void populateToken3() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 3);
        assertNotNull(token);
        int length = token.getTokenWord().getLength();
        log(format("Token word length: %s", length), true);
        Location location = token.getLocations().get(0);
        NounProperties properties = (NounProperties) location.getProperties();
        properties.setNounType(DEFINITE);
        properties.setGender(MASCULINE);
        properties.setNumber(SINGULAR);
        properties.setStatus(GENETIVE);
        repositoryUtil.getLocationRepository().save(location);
    }

    @Test(dependsOnMethods = "populateToken3")
    public void createChapter2() {
        int chapterNumber = 2;
        log(format("Creating chapter %s", chapterNumber), true);
        repositoryUtil.createChapter(chapterNumber);
        log(format("Created chapter %s", chapterNumber), true);
    }

    @Test(dependsOnMethods = "createChapter2")
    public void getRangeOfVerses() {
        int chapterNumber = 2;
        int from = 10;
        int to = 21;
        int len = to - from - 1;
        VerseRepository verseRepository = repositoryUtil.getVerseRepository();
        List<Verse> verses = verseRepository.findByChapterNumberAndVerseNumberBetween(chapterNumber, from, to);
        assertNotNull(verses);
        assertEquals(verses.isEmpty(), false);
        assertEquals(verses.size(), len);
        for (Verse verse : verses) {
            log(format("VERSE: %s", verse.getDisplayName()), true);
        }
    }

    @Test(dependsOnMethods = "getRangeOfVerses")
    public void loadLocation() {
        LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        Location location = locationRepository.findByDisplayName(format("%s:%s:%s:%s",
                DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 4, 2));
        log(format("Location found: %s (%s)", location, location.getLocationWord().toBuckWalter()), true);
    }

    @Test(dependsOnMethods = "loadLocation")
    public void testQuery() {
        BasicQuery basicQuery = new BasicQuery("{\"displayName\" : \"1:2:1\"}");
        Token token = repositoryUtil.getMongoTemplate().findOne(basicQuery, Token.class);
        assertNotNull(token);
        log(format("Token found: %s", token));
    }

    @Test(dependsOnMethods = "testQuery")
    public void createTerminalNode4() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 4);
        assertNotNull(token);
        TerminalNode terminalNode = new TerminalNode(token);
        terminalNode = repositoryUtil.getTerminalNodeRepository().save(terminalNode);
        assertNotNull(terminalNode);
        log(format("Created TerminalNode: %s", terminalNode.getDisplayName()));
    }

    @Test(dependsOnMethods = "createTerminalNode4")
    public void createTerminalNode3() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 3);
        assertNotNull(token);
        TerminalNode terminalNode = new TerminalNode(token);
        terminalNode = repositoryUtil.getTerminalNodeRepository().save(terminalNode);
        assertNotNull(terminalNode);
        log(format("Created TerminalNode: %s", terminalNode.getDisplayName()));
    }

    @Test(dependsOnMethods = "createTerminalNode3")
    public void createPartOfSpeechNodes() {
        TerminalNodeRepository terminalNodeRepository = repositoryUtil.getTerminalNodeRepository();
        TerminalNode terminalNode = terminalNodeRepository.findByDisplayName("1:2:4:TERMINAL");
        assertNotNull(terminalNode);
        List<Location> locations = terminalNode.getToken().getLocations();
        reverse(locations);
        for (Location location : locations) {
            PartOfSpeechNode partOfSpeechNode = new PartOfSpeechNode(location);
            terminalNode.getPartOfSpeechNodes().add(partOfSpeechNode);
        }
        terminalNode = terminalNodeRepository.save(terminalNode);
        assertEquals(terminalNode.getPartOfSpeechNodes().size(), locations.size());
    }

    @Test(dependsOnMethods = "createPartOfSpeechNodes")
    public void createTerminalNode3V1() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 3);
        assertNotNull(token);
        TerminalNode terminalNode = new TerminalNode(token);
        terminalNode.setVersion(1);
        terminalNode = repositoryUtil.getTerminalNodeRepository().save(terminalNode);
        assertNotNull(terminalNode);
        log(format("Created TerminalNode: %s", terminalNode.getDisplayName()));
    }

    @Test(dependsOnMethods = "createTerminalNode3V1")
    public void countTerminalNodes() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 3);
        assertNotNull(token);
        TerminalNodeRepository terminalNodeRepository = repositoryUtil.getTerminalNodeRepository();
        Long count = terminalNodeRepository.countByChapterNumberAndVerseNumberAndTokenNumber(
                token.getChapterNumber(), token.getVerseNumber(), token.getTokenNumber());
        assertEquals(count.intValue(), 2);
        log(format("Total number of terminal nodes found: %s", count));
    }

    @Test(dependsOnMethods = "countTerminalNodes")
    public void saveIncompleteVerbType() {
        Location location = new Location(0, 1, 1, 1);
        location.setPartOfSpeech(VERB);
        VerbProperties vp = (VerbProperties) location.getProperties();
        vp.setVerbType(IMPERFECT);
        vp.setConversationType(THIRD_PERSON);
        vp.setGender(MASCULINE);
        vp.setNumber(SINGULAR);
        KanaAndSisters incompleteVerb = new KanaAndSisters();
        incompleteVerb.setType(MEMBER1);
        vp.setIncompleteVerb(incompleteVerb);

        repositoryUtil.getLocationRepository().save(location);
    }

    @Test(dependsOnMethods = "saveIncompleteVerbType")
    public void retrieveIncompleteVerbType() {
        Location location = repositoryUtil.getLocationRepository().findByDisplayName("0:1:1:1");
        assertNotNull(location);
        assertEquals(location.getPartOfSpeech(), VERB);
        VerbProperties vp = (VerbProperties) location.getProperties();
        log(format("Incomplete Verb Type: %s", vp.getIncompleteVerb().getType()));
    }
}
