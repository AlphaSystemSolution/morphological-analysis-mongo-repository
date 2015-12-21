package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType.MASCULINE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus.GENETIVE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounType.DEFINITE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType.PLURAL;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.DEFINITE_ARTICLE;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.testng.Assert.*;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class MorphologicalAnalysisBasicTest extends AbstractTestNGSpringContextTests {

    private static final int DEFAULT_CHAPTER_NUMBER = 1;
    private static final int DEFAULT_VERSE_NUMBER = 2;

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @BeforeClass
    public void beforeSuite() {
        log("Checking database", true);
        String dbName = getProperty(MongoConfig.MONGO_DB_NAME_PROPERTY);
        log(format("Database in use {%s}", dbName), true);
        assertTrue("MORPHOLOGICAL_ANALYSIS_TEST_DB".equals(dbName));
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
        location.setMorphologicalEntry(new MorphologicalEntry());
        NounProperties properties = (NounProperties) location.getProperties();
        properties.setNounType(DEFINITE);
        properties.setGender(MASCULINE);
        properties.setNumber(PLURAL);
        properties.setStatus(GENETIVE);
        repositoryUtil.getLocationRepository().save(location);
        token.getLocations().add(location);
        repositoryUtil.getTokenRepository().save(token);
    }
}
