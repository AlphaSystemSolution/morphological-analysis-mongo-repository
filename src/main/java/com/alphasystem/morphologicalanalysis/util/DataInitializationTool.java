package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.wordbyword.exception.InvalidChapterException;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.ChapterRepository;
import com.alphasystem.tanzil.model.Document;
import com.alphasystem.tanzil.model.Verse;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.JAXBTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sali
 */
@Component
public class DataInitializationTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializationTool.class);
    private static final int FIRST_CHAPTER_NUMBER = 1;
    private static final int LAST_CHAPTER_NUMBER = 114;

    @Autowired private ChapterRepository chapterRepository;
    private Map<Script, Document> documentMap = new LinkedHashMap<>();

    private Document getDocument(Script script) throws IOException, URISyntaxException, JAXBException {
        Document document = documentMap.get(script);
        if (document == null) {
            JAXBTool jaxbTool = new JAXBTool();
            final URL url = AppUtil.getPath(script.getPath()).toUri().toURL();
            LOGGER.debug("Script URL: {}", url.toString());
            document = jaxbTool.unmarshal(Document.class, url);
            documentMap.put(script, document);
        }
        return document;
    }

    public void createAllChapters(Script script) throws URISyntaxException, IOException, JAXBException {
        for (int chapterNumber = FIRST_CHAPTER_NUMBER; chapterNumber <= LAST_CHAPTER_NUMBER; chapterNumber++) {
            try {
                createChapter(chapterNumber, script);
            } catch (InvalidChapterException e) {
                // ignore, do nothing this should never happen
            }
        }
    }

    public void createChapter(int chapterNumber, Script script) throws InvalidChapterException, JAXBException, IOException,
            URISyntaxException {
        if (chapterNumber < FIRST_CHAPTER_NUMBER || chapterNumber > LAST_CHAPTER_NUMBER) {
            throw new InvalidChapterException(chapterNumber);
        }
        LOGGER.info("Start creating chapter {}", chapterNumber);
        Document document = getDocument(script);
        com.alphasystem.tanzil.model.Chapter ch = document.getChapters().get(chapterNumber - 1);
        Chapter chapter = new Chapter(chapterNumber, ch.getName());
        List<Verse> verses = ch.getVerses();
        int verseCount = verses.size();
        chapter.setVerseCount(verseCount);
        for (int verseNumber = 1; verseNumber <= verseCount; verseNumber++) {
            chapter.addVerse(createVerse(chapterNumber, null, verses.get(verseNumber - 1)));
        } // end of verse loop
        chapterRepository.save(chapter);
        LOGGER.info("Finished creating chapter {}", chapterNumber);
    }

    public com.alphasystem.morphologicalanalysis.wordbyword.model.Verse createVerse(int chapterNumber,
                                                                                    com.alphasystem.morphologicalanalysis.wordbyword.model.Verse verse,
                                                                                    Verse vs) {
        int verseNumber = vs.getVerseNumber();
        LOGGER.debug("Start creating verse {}", verseNumber);
        if (verse == null) {
            verse = new com.alphasystem.morphologicalanalysis.wordbyword.model.Verse(chapterNumber, verseNumber);
        }
        verse.setText(vs.getText());
        verse.setTokenCount(0);
        verse.setTokens(null);

        int tokenNumber = 1;
        List<ArabicWord> tokens = vs.getTokens();
        for (ArabicWord aw : tokens) {
            final String text = aw.toUnicode();
            Token token = new Token(chapterNumber, verseNumber, tokenNumber, text);
            LOGGER.debug("Token \"{}\" created with text \"{}\".", token, token.tokenWord().toUnicode());
            // we will create one location for each token
            Location location = new Location(chapterNumber, verseNumber, tokenNumber, 1, WordType.NOUN);
            location.setStartIndex(0);
            location.setEndIndex(aw.getLength());
            location.setText(text);
            location.setDerivedText(text);
            token.addLocation(location);
            verse.addToken(token);
            tokenNumber++;
        } // end of token loop
        verse.setTokenCount(verse.getTokens().size());
        LOGGER.debug("Finished creating verse {}", verseNumber);
        return verse;
    }
}
