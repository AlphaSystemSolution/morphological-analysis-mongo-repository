package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.arabic.model.ProNoun;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbType.IMPERFECT;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbType.PERFECT;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class SupportDataCreation extends AbstractTestNGSpringContextTests {

    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    private static Token createPronounToken(Integer tokenNumber, Integer verseNumber, ProNoun proNoun) {
        Token token = new Token().withChapterNumber(0).withVerseNumber(verseNumber).withTokenNumber(tokenNumber)
                .withHidden(true).withToken(format("(%s)", proNoun.getLabel().toUnicode()));
        token.setId(proNoun.name());

        Location location = new Location().withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(PRONOUN).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength());
        System.out.println(format("creating hidden location: %s (POS: %s)", location, location.getPartOfSpeech()));

        token.setLocations(singletonList(location));

        System.out.println(format("Creating hidden token: %s", token));
        return token;
    }

    @Autowired
    public void setRepositoryUtil(MorphologicalAnalysisRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
    }

    private void createToken(String id, Integer verseNumber, Integer tokenNumber, PartOfSpeech partOfSpeech,
                             AbstractProperties properties) {
        Token token = new Token().withChapterNumber(0).withVerseNumber(verseNumber).withTokenNumber(tokenNumber)
                .withHidden(true).withToken("(*)");
        token.setId(id);

        Location location = new Location().withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(partOfSpeech).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength()).withProperties(properties);
        location.setId(id);
        System.out.println(format("creating implied location: %s (POS: %s)", location, location.getPartOfSpeech()));

        token.setLocations(singletonList(location));

        System.out.println(format("Creating implied token: %s", token));
        repositoryUtil.getTokenRepository().save(token);
    }

    @Test
    public void createNounTokens() {
        int verseNumber = 1;
        int tokenNumber = 1;
        AbstractNounProperties properties = new NounProperties().withNounStatus(NOMINATIVE);
        String id = format("%s_%s", NOUN, properties.getStatus());
        createToken(id, verseNumber, tokenNumber, NOUN, properties);

        tokenNumber = 2;
        properties = new NounProperties().withNounStatus(ACCUSATIVE);
        id = format("%s_%s", NOUN, properties.getStatus());
        createToken(id, verseNumber, tokenNumber, NOUN, properties);

        tokenNumber = 3;
        properties = new NounProperties().withNounStatus(GENETIVE);
        id = format("%s_%s", NOUN, properties.getStatus());
        createToken(id, verseNumber, tokenNumber, NOUN, properties);
    }

    @Test(dependsOnMethods = {"createNounTokens"})
    public void createVerbTokens() {
        int verseNumber = 2;
        int tokenNumber = 1;
        VerbProperties properties = new VerbProperties().withVerbType(PERFECT);
        String id = format("%s_%s", VERB, properties.getVerbType());
        createToken(id, verseNumber, tokenNumber, VERB, properties);

        tokenNumber = 2;
        properties = new VerbProperties().withVerbType(IMPERFECT);
        id = format("%s_%s", VERB, properties.getVerbType());
        createToken(id, verseNumber, tokenNumber, VERB, properties);
    }

    @Test(dependsOnMethods = {"createVerbTokens"})
    public void createPronounTokens() {
        List<Token> tokens = new ArrayList<>();
        int verseNumber = 3;
        int tokenNumber = 1;
        for (ProNoun proNoun : ProNoun.values()) {
            tokens.add(createPronounToken(tokenNumber, verseNumber, proNoun));
            tokenNumber++;
        }
        repositoryUtil.getTokenRepository().save(tokens);
    }


}
