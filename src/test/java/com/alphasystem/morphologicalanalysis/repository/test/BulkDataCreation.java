/**
 *
 */
package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.arabic.model.ProNoun;
import com.alphasystem.morphologicalanalysis.spring.support.GraphConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.spring.support.WordByWordConfig;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.*;
import static com.alphasystem.util.Utils.getTimeConsumed;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
        MorphologicalAnalysisSpringConfiguration.class})
public class BulkDataCreation extends AbstractTestNGSpringContextTests {

    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    /**
     * Creates initial data.
     */
    @Test
    public void createChapters() {
        long startTime = currentTimeMillis();
        for (int chapterNumber = 1; chapterNumber <= 114; chapterNumber++) {
            long chapterStartTime = currentTimeMillis();
            getRepositoryUtil().createChapter(chapterNumber);
            long chapterEndTime = currentTimeMillis();
            out.printf("Time consume to save chapter {%s} is {%s}",
                    chapterNumber, getTimeConsumed(chapterEndTime
                            - chapterStartTime));
            out.println();
        }
        long endTime = currentTimeMillis();
        out.printf("Total time consume is {%s}", getTimeConsumed(endTime
                - startTime));
        out.println();
    }

    @Test(dependsOnMethods = {"createChapters"})
    public void createImpliedTokens() {
        Token token = new Token().withChapterNumber(0).withVerseNumber(1).withTokenNumber(1).withToken("(*)")
                .withHidden(true);

        Location location = new Location().withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(NOUN).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength());
        System.out.println(format("creating implied location: %s (POS: %s)", location, location.getPartOfSpeech()));

        token.setLocations(singletonList(location));

        System.out.println(format("Creating implied token: %s", token));
        repositoryUtil.getTokenRepository().save(token);

        token = new Token().withChapterNumber(0).withVerseNumber(1).withTokenNumber(2).withToken("(*)")
                .withHidden(true);

        location = new Location().withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(VERB).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength());
        System.out.println(format("creating implied location: %s (POS: %s)", location, location.getPartOfSpeech()));

        token.setLocations(singletonList(location));

        System.out.println(format("Creating implied token: %s", token));
        repositoryUtil.getTokenRepository().save(token);
    }

    @Test(dependsOnMethods = {"createImpliedTokens"})
    public void createPronounTokens() {
        List<Token> tokens = new ArrayList<>();
        int tokenNumber = 3;
        for (ProNoun proNoun : ProNoun.values()) {
            tokens.add(createToken(tokenNumber, proNoun));
            tokenNumber++;
        }
        repositoryUtil.getTokenRepository().save(tokens);
    }

    public MorphologicalAnalysisRepositoryUtil getRepositoryUtil() {
        return repositoryUtil;
    }

    @Autowired
    public void setRepositoryUtil(
            MorphologicalAnalysisRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
    }

    private Token createToken(Integer tokenNumber, ProNoun proNoun) {
        Token token = new Token().withChapterNumber(0).withVerseNumber(1).withTokenNumber(tokenNumber)
                .withHidden(true).withToken(format("(%s)", proNoun.getLabel().toUnicode()));
        token.setId(proNoun.name());

        Location location = new Location().withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(PRONOUN).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength());
        System.out.println(format("creating hidden pronoun location: %s (POS: %s)", location,
                location.getPartOfSpeech()));

        token.setLocations(singletonList(location));

        return token;
    }

}
