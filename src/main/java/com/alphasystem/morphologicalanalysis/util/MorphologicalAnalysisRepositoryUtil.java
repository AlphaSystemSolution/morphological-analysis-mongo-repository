/**
 *
 */
package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.FragmentRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.RelationshipRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.ChapterRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.util.ChapterComparator;
import com.alphasystem.tanzil.TanzilTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Collections.sort;

/**
 * @author sali
 */
@Component
public class MorphologicalAnalysisRepositoryUtil {

    private MongoTemplate mongoTemplate;
    private ChapterRepository chapterRepository;
    private VerseRepository verseRepository;
    private TokenRepository tokenRepository;
    private LocationRepository locationRepository;
    private FragmentRepository fragmentRepository;
    private RelationshipRepository relationshipRepository;
    private DependencyGraphRepository dependencyGraphRepository;
    private TanzilTool tanzilTool;
    private Query findAllChaptersQuery;
    private boolean verbose;

    public MorphologicalAnalysisRepositoryUtil() {
        tanzilTool = TanzilTool.getInstance();
        Query findAllChaptersQuery = new Query();
        findAllChaptersQuery.fields().include("chapterNumber")
                .include("verseCount").include("chapterName");
    }

    // Business methods

    public void createChapter(int chapterNumber) {
        if (verbose) {
            out.println(format("Start creating chapter {%s}", chapterNumber));
        }
        com.alphasystem.tanzil.model.Chapter ch = tanzilTool.getChapter(chapterNumber);
        Chapter chapter = new Chapter(chapterNumber, ch.getName());
        List<com.alphasystem.tanzil.model.Verse> verses = ch.getVerses();
        int verseCount = verses.size();
        chapter.setVerseCount(verseCount);
        for (int verseNumber = 1; verseNumber <= verseCount; verseNumber++) {
            com.alphasystem.tanzil.model.Verse vs = verses.get(verseNumber - 1);
            if (verbose) {
                out.println(format("Start creating verse {%s}", verseNumber));
            }
            Verse verse = new Verse(chapterNumber, verseNumber);
            int tokenNumber = 1;
            List<ArabicWord> tokens = vs.getTokens();
            for (ArabicWord aw : tokens) {
                Token token = new Token(chapterNumber, verseNumber, tokenNumber, aw.toUnicode());
                // we will create one location for each token
                Location location = new Location(chapterNumber, verseNumber, tokenNumber, 1);
                locationRepository.save(location);
                token.addLocation(location);
                tokenRepository.save(token);
                verse.addToken(token);
                tokenNumber++;
            } // end of token loop
            verseRepository.save(verse);
            if (verbose) {
                out.println(format("Finished creating verse {%s}", verseNumber));
            }
            chapter.addVerse(verse);
        } // end of verse loop
        chapterRepository.save(chapter);
        if (verbose) {
            out.println(format("Finished creating chapter {%s}", chapterNumber));
        }
    }

    /**
     * @return
     */
    public List<Chapter> findAllChapters() {
        List<Chapter> chapters = mongoTemplate.find(findAllChaptersQuery,
                Chapter.class, Chapter.class.getSimpleName().toLowerCase());
        sort(chapters, new ChapterComparator());
        return chapters;
    }

    // Getter & Setters
    public ChapterRepository getChapterRepository() {
        return chapterRepository;
    }

    @Autowired
    public void setChapterRepository(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public LocationRepository getLocationRepository() {
        return locationRepository;
    }

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Autowired
    public void setMongoTemplate(@Qualifier("wordByWordTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public TokenRepository getTokenRepository() {
        return tokenRepository;
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public VerseRepository getVerseRepository() {
        return verseRepository;
    }

    @Autowired
    public void setVerseRepository(VerseRepository verseRepository) {
        this.verseRepository = verseRepository;
    }

    public RelationshipRepository getRelationshipRepository() {
        return relationshipRepository;
    }

    @Autowired
    public void setRelationshipRepository(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    public FragmentRepository getFragmentRepository() {
        return fragmentRepository;
    }

    @Autowired
    public void setFragmentRepository(FragmentRepository fragmentRepository) {
        this.fragmentRepository = fragmentRepository;
    }

    public DependencyGraphRepository getDependencyGraphRepository() {
        return dependencyGraphRepository;
    }

    @Autowired
    public void setDependencyGraphRepository(DependencyGraphRepository dependencyGraphRepository) {
        this.dependencyGraphRepository = dependencyGraphRepository;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
