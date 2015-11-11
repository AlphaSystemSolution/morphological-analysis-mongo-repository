package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.spring.support.GraphConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.spring.support.WordByWordConfig;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
        MorphologicalAnalysisSpringConfiguration.class})
public class TokenListGenerator extends AbstractTestNGSpringContextTests {

    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @Autowired
    public void setRepositoryUtil(MorphologicalAnalysisRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
    }

    @Test
    public void generateTokenList() {
        List<String> lines = new ArrayList<>();
        VerseRepository verseRepository = repositoryUtil.getVerseRepository();
        List<Chapter> chapters = repositoryUtil.findAllChapters();
        chapters.forEach(chapter -> {
            lines.add(valueOf(chapter.getChapterNumber()));
            List<Verse> verses = verseRepository.findByChapterNumber(chapter.getChapterNumber());
            verses.forEach(verse -> {
                Integer tokenCount = verse.getTokenCount();
                lines.add(format("%s:%s:%s", verse.getVerseNumber(), 1, tokenCount));
            });
        });
        try {
            Files.write(Paths.get("verse-tokens-pairs.txt"), lines, CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
