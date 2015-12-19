/**
 *
 */
package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.common.model.QVerseTokensPair;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.QDependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.graph.repository.*;
import com.alphasystem.morphologicalanalysis.morphology.repository.MorphologicalEntryRepository;
import com.alphasystem.morphologicalanalysis.morphology.repository.RootLettersRepository;
import com.alphasystem.morphologicalanalysis.morphology.repository.RootWordRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.ChapterRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.util.ChapterComparator;
import com.alphasystem.tanzil.TanzilTool;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.ListPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.tanzil.QuranScript.QURAN_SIMPLE_ENHANCED;
import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Collections.sort;

/**
 * @author sali
 */
@Component
public class MorphologicalAnalysisRepositoryUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MorphologicalAnalysisRepositoryUtil.class);

    private MongoTemplate mongoTemplate;
    private ChapterRepository chapterRepository;
    private VerseRepository verseRepository;
    private TokenRepository tokenRepository;
    private LocationRepository locationRepository;
    private DependencyGraphRepository dependencyGraphRepository;
    private TerminalNodeRepository terminalNodeRepository;
    private ImpliedNodeRepository impliedNodeRepository;
    private HiddenNodeRepository hiddenNodeRepository;
    private ReferenceNodeRepository referenceNodeRepository;
    private PartOfSpeechNodeRepository partOfSpeechNodeRepository;
    private PhraseNodeRepository phraseNodeRepository;
    private RelationshipNodeRepository relationshipNodeRepository;
    private RootLettersRepository rootLettersRepository;
    private RootWordRepository rootWordRepository;
    private MorphologicalEntryRepository morphologicalEntryRepository;
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
        com.alphasystem.tanzil.model.Chapter ch = tanzilTool.getChapter(chapterNumber, QURAN_SIMPLE_ENHANCED);
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

    public List<Token> getTokens(VerseTokenPairGroup group) {
        List<VerseTokensPair> pairs = group.getPairs();
        if (pairs == null | pairs.isEmpty()) {
            return new ArrayList<>();
        }
        VerseTokensPair pair = pairs.get(0);
        QToken qToken = QToken.token1;
        BooleanExpression predicate = qToken.verseNumber.eq(pair.getVerseNumber()).
                and(qToken.tokenNumber.between(pair.getFirstTokenIndex(), pair.getLastTokenIndex()));
        for (int i = 1; i < pairs.size(); i++) {
            pair = pairs.get(i);
            BooleanExpression predicate1 = qToken.verseNumber.eq(pair.getVerseNumber())
                    .and(qToken.tokenNumber.between(pair.getFirstTokenIndex(), pair.getLastTokenIndex()));
            predicate = predicate.or(predicate1);
        }
        predicate = qToken.chapterNumber.eq(group.getChapterNumber()).and(predicate);
        if (group.isIncludeHidden()) {
            predicate = predicate.and(qToken.hidden.eq(true));
        }
        LOGGER.info(format("Query for \"getTokens\" is {%s}", predicate));
        return (List<Token>) tokenRepository.findAll(predicate);
    }

    public List<DependencyGraph> getDependencyGraphs(VerseTokenPairGroup group) {
        List<VerseTokensPair> pairs = group.getPairs();
        if (pairs == null || pairs.isEmpty()) {
            return new ArrayList<>();
        }
        QDependencyGraph qDependencyGraph = QDependencyGraph.dependencyGraph;
        int index = 0;
        VerseTokensPair pair = pairs.get(index);
        ListPath<VerseTokensPair, QVerseTokensPair> tokens = qDependencyGraph.tokens;
        BooleanExpression predicate = tokens.get(index).verseNumber.eq(pair.getVerseNumber());
        for (index = 1; index < pairs.size(); index++) {
            pair = pairs.get(index);
            BooleanExpression predicate1 = tokens.get(index).verseNumber.eq(pair.getVerseNumber());
            predicate = predicate.or(predicate1);
        }
        predicate = qDependencyGraph.chapterNumber.eq(group.getChapterNumber()).and(predicate);
        LOGGER.info(format("Query for \"getDependencyGraphs\" is {%s}", predicate));
        return (List<DependencyGraph>) dependencyGraphRepository.findAll(predicate);
    }

    public DependencyGraph getDependencyGraph(String displayName) {
        return dependencyGraphRepository.findByDisplayName(displayName);
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
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
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

    public ImpliedNodeRepository getImpliedNodeRepository() {
        return impliedNodeRepository;
    }

    @Autowired
    public void setImpliedNodeRepository(ImpliedNodeRepository impliedNodeRepository) {
        this.impliedNodeRepository = impliedNodeRepository;
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

    public RootLettersRepository getRootLettersRepository() {
        return rootLettersRepository;
    }

    @Autowired
    public void setRootLettersRepository(RootLettersRepository rootLettersRepository) {
        this.rootLettersRepository = rootLettersRepository;
    }

    public RootWordRepository getRootWordRepository() {
        return rootWordRepository;
    }

    @Autowired
    public void setRootWordRepository(RootWordRepository rootWordRepository) {
        this.rootWordRepository = rootWordRepository;
    }

    public MorphologicalEntryRepository getMorphologicalEntryRepository() {
        return morphologicalEntryRepository;
    }

    @Autowired
    public void setMorphologicalEntryRepository(MorphologicalEntryRepository morphologicalEntryRepository) {
        this.morphologicalEntryRepository = morphologicalEntryRepository;
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
            case IMPLIED:
                repository = getImpliedNodeRepository();
                break;
            case ROOT:
                break;
        }
        return repository;
    }

    @SuppressWarnings({"unchecked"})
    public void delete(GraphNode graphNode) {
        if (graphNode == null) {
            return;
        }
        GraphNodeType graphNodeType = graphNode.getGraphNodeType();
        switch (graphNodeType) {
            case TERMINAL:
            case IMPLIED:
            case REFERENCE:
            case HIDDEN:
                TerminalNode tn = (TerminalNode) graphNode;
                tn.getPartOfSpeechNodes().forEach(partOfSpeechNode -> {
                    if (partOfSpeechNode != null) {
                        getPartOfSpeechNodeRepository().delete(partOfSpeechNode.getId());
                    }
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
