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

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.PRONOUN;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
        MorphologicalAnalysisSpringConfiguration.class})
public class PronounDataCreation extends AbstractTestNGSpringContextTests {

    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    private Token createToken(Integer tokenNumber, ProNoun proNoun){
        Token token = new Token().withChapterNumber(0).withVerseNumber(1).withTokenNumber(tokenNumber)
                .withHidden(true).withToken(format("(%s)", proNoun.getLabel().toUnicode()));
        token.setId(proNoun.name());

        Location location = new Location().withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(PRONOUN).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength());

        token.setLocations(singletonList(location));

        return token;
    }

    @Test
    public void createPronounTokens(){
        List<Token> tokens = new ArrayList<>();
        int tokenNumber = 3;
        for (ProNoun proNoun : ProNoun.values()) {
            tokens.add(createToken(tokenNumber, proNoun));
            tokenNumber++;
        }
        repositoryUtil.getTokenRepository().save(tokens);
    }

    @Autowired
    public void setRepositoryUtil(MorphologicalAnalysisRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
    }


}
