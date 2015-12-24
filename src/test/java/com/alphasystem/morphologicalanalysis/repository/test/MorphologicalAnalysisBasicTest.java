package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.testng.Assert.*;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class MorphologicalAnalysisBasicTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;
    private TokenRepository tokenRepository;

    @BeforeClass
    public void beforeSuite() {
        log("Checking database", true);
        String dbName = getProperty(MongoConfig.MONGO_DB_NAME_PROPERTY);
        log(format("Database in use {%s}", dbName), true);
        assertTrue("MORPHOLOGICAL_ANALYSIS_TEST_DB".equals(dbName));
        tokenRepository = repositoryUtil.getTokenRepository();
        assertNotNull(tokenRepository);
    }

    @Test
    public void getPreviousTokenPositiveCase() {
        Integer chapterNumber = 1;
        Integer verseNumber = 5;
        Integer tokenNumber = 3;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting previous token for token {%s}", dummy), true);

        Token token = repositoryUtil.getPreviousToken(dummy);
        log(format("Previous token found {%s}", token), true);
        assertNotNull(token);
        assertEquals((Object) token.getTokenNumber(), tokenNumber - 1);
    }

    @Test
    public void getPreviousTokenLowerBoundary() {
        Integer chapterNumber = 1;
        Integer verseNumber = 1;
        Integer tokenNumber = 1;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting previous token for token {%s}", dummy));

        Token token = repositoryUtil.getPreviousToken(dummy);
        log(format("No previous token found for {%s}", token), true);
        assertNull(token);
    }

    @Test
    public void getPreviousTokenFirstTokenFirstVerseOfChapter() {
        Integer chapterNumber = 2;
        Integer verseNumber = 1;
        Integer tokenNumber = 1;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting previous token for token {%s}", dummy));

        Token token = repositoryUtil.getPreviousToken(dummy);
        assertNotNull(token);
        log(format("Previous token for {%s} is {%s}", dummy, token), true);
        assertEquals(token.getDisplayName(), "1:7:9");
    }

    @Test
    public void getPreviousTokenFirstTokenOfMiddleVerseOfChapter() {
        Integer chapterNumber = 1;
        Integer verseNumber = 5;
        Integer tokenNumber = 1;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting previous token for token {%s}", dummy));

        Token token = repositoryUtil.getPreviousToken(dummy);
        assertNotNull(token);
        log(format("Previous token for {%s} is {%s}", dummy, token), true);
        assertEquals(token.getDisplayName(), "1:4:3");
    }

    @Test
    public void getNextTokenPositiveCase() {
        Integer chapterNumber = 1;
        Integer verseNumber = 5;
        Integer tokenNumber = 3;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting next token for token {%s}", dummy), true);

        Token token = repositoryUtil.getNextToken(dummy);
        log(format("Next token found {%s}", token), true);
        assertNotNull(token);
        assertEquals((Object) token.getTokenNumber(), tokenNumber + 1);
    }

    @Test
    public void getNextTokenUpperBoundary() {
        Integer chapterNumber = 114;
        Integer verseNumber = 6;
        Integer tokenNumber = 3;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting next token for token {%s}", dummy));

        Token token = repositoryUtil.getNextToken(dummy);
        log(format("No next token found for {%s}", token), true);
        assertNull(token);
    }

    @Test
    public void getNextTokenLastTokenLastVerseOfChapter() {
        Integer chapterNumber = 1;
        Integer verseNumber = 7;
        Integer tokenNumber = 9;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting next token for token {%s}", dummy));

        Token token = repositoryUtil.getNextToken(dummy);
        assertNotNull(token);
        log(format("Next token for {%s} is {%s}", dummy, token), true);
        assertEquals(token.getDisplayName(), "2:1:1");
    }

    @Test
    public void getNextTokenLastTokenOfMiddleVerseOfChapter() {
        Integer chapterNumber = 1;
        Integer verseNumber = 5;
        Integer tokenNumber = 4;
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        log(format("Getting next token for token {%s}", dummy));

        Token token = repositoryUtil.getNextToken(dummy);
        assertNotNull(token);
        log(format("Previous token for {%s} is {%s}", dummy, token), true);
        assertEquals(token.getDisplayName(), "1:6:1");
    }

}
