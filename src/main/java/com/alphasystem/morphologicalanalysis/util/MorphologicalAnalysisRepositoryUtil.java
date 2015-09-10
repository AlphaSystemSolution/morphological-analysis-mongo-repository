/**
 *
 */
package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.graph.repository.*;
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
    private DependencyGraphRepository dependencyGraphRepository;
    private TerminalNodeRepository terminalNodeRepository;
    private EmptyNodeRepository emptyNodeRepository;
    private HiddenNodeRepository hiddenNodeRepository;
    private ReferenceNodeRepository referenceNodeRepository;
    private PartOfSpeechNodeRepository partOfSpeechNodeRepository;
    private PhraseNodeRepository phraseNodeRepository;
    private RelationshipNodeRepository relationshipNodeRepository;
    private TanzilTool tanzilTool;
    private Query findAllChaptersQuery;
    private boolean verbose;

    public MorphologicalAnalysisRepositoryUtil() {
        tanzilTool = TanzilTool.getInstance();
        findAllChaptersQuery = new Query();
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
                // locationRepository.save(location);
                token.addLocation(location);
                // tokenRepository.save(token);
                verse.addToken(token);
                tokenNumber++;
            } // end of token loop
            // verseRepository.save(verse);
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

    public DependencyGraphRepository getDependencyGraphRepository() {
        return dependencyGraphRepository;
    }

    @Autowired
    public void setDependencyGraphRepository(DependencyGraphRepository dependencyGraphRepository) {
        this.dependencyGraphRepository = dependencyGraphRepository;
    }

    public TerminalNodeRepository getTerminalNodeRepository() {
        return terminalNodeRepository;
    }

    @Autowired
    public void setTerminalNodeRepository(TerminalNodeRepository terminalNodeRepository) {
        this.terminalNodeRepository = terminalNodeRepository;
    }

    public EmptyNodeRepository getEmptyNodeRepository() {
        return emptyNodeRepository;
    }

    @Autowired
    public void setEmptyNodeRepository(EmptyNodeRepository emptyNodeRepository) {
        this.emptyNodeRepository = emptyNodeRepository;
    }

    public HiddenNodeRepository getHiddenNodeRepository() {
        return hiddenNodeRepository;
    }

    @Autowired
    public void setHiddenNodeRepository(HiddenNodeRepository hiddenNodeRepository) {
        this.hiddenNodeRepository = hiddenNodeRepository;
    }

    public ReferenceNodeRepository getReferenceNodeRepository() {
        return referenceNodeRepository;
    }

    @Autowired
    public void setReferenceNodeRepository(ReferenceNodeRepository referenceNodeRepository) {
        this.referenceNodeRepository = referenceNodeRepository;
    }

    public PartOfSpeechNodeRepository getPartOfSpeechNodeRepository() {
        return partOfSpeechNodeRepository;
    }

    @Autowired
    public void setPartOfSpeechNodeRepository(PartOfSpeechNodeRepository partOfSpeechNodeRepository) {
        this.partOfSpeechNodeRepository = partOfSpeechNodeRepository;
    }

    public PhraseNodeRepository getPhraseNodeRepository() {
        return phraseNodeRepository;
    }

    @Autowired
    public void setPhraseNodeRepository(PhraseNodeRepository phraseNodeRepository) {
        this.phraseNodeRepository = phraseNodeRepository;
    }

    public RelationshipNodeRepository getRelationshipNodeRepository() {
        return relationshipNodeRepository;
    }

    @Autowired
    public void setRelationshipNodeRepository(RelationshipNodeRepository relationshipNodeRepository) {
        this.relationshipNodeRepository = relationshipNodeRepository;
    }

    public GraphNodeRepository getRepository(GraphNodeType nodeType) {
        GraphNodeRepository repository = null;
        switch (nodeType) {
            case TERMINAL:
                repository = getTerminalNodeRepository();
                break;
            case PART_OF_SPEECH:
                repository = getPartOfSpeechNodeRepository();
                break;
            case PHRASE:
                repository = getPhraseNodeRepository();
                break;
            case RELATIONSHIP:
                repository = getRelationshipNodeRepository();
                break;
            case REFERENCE:
                repository = getReferenceNodeRepository();
                break;
            case HIDDEN:
                repository = getHiddenNodeRepository();
                break;
            case EMPTY:
                repository = getEmptyNodeRepository();
                break;
            case ROOT:
                break;
        }
        return repository;
    }

    public void delete(GraphNode graphNode) {
        if (graphNode == null) {
            return;
        }
        GraphNodeType graphNodeType = graphNode.getGraphNodeType();
        switch (graphNodeType) {
            case TERMINAL:
            case EMPTY:
            case REFERENCE:
            case HIDDEN:
                TerminalNode tn = (TerminalNode) graphNode;
                tn.getPartOfSpeechNodes().forEach(partOfSpeechNode -> {
                    getPartOfSpeechNodeRepository().delete(partOfSpeechNode.getId());
                });
                break;
        }
        GraphNodeRepository repository = getRepository(graphNodeType);
        repository.delete(graphNode.getId());
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
