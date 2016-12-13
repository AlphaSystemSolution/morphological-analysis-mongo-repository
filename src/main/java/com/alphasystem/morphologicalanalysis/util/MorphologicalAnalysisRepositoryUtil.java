package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.common.model.QVerseTokensPair;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.QDependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.graph.repository.*;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.repository.DictionaryNotesRepository;
import com.alphasystem.morphologicalanalysis.morphology.repository.MorphologicalEntryRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.ChapterRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.util.ChapterComparator;
import com.alphasystem.tanzil.TanzilTool;
import com.alphasystem.tanzil.model.Document;
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
import java.util.Map;

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
    private MorphologicalEntryRepository morphologicalEntryRepository;
    private DictionaryNotesRepository dictionaryNotesRepository;
    private TanzilTool tanzilTool;
    private Query findAllChaptersQuery;
    private boolean verbose;

    public MorphologicalAnalysisRepositoryUtil() {
        tanzilTool = TanzilTool.getInstance();
        findAllChaptersQuery = new Query();
        findAllChaptersQuery.fields().include("chapterNumber")
                .include("verseCount").include("chapterName");
    }

    private static Token getToken(Integer chapterNumber, Integer verseNumber, Integer tokenNumber, boolean next,
                                  TokenRepository tokenRepository, MorphologicalAnalysisRepositoryUtil repositoryUtil) {
        LOGGER.debug("Getting request to find token {}:{}:{}", chapterNumber, verseNumber, tokenNumber);
        if (chapterNumber <= 0 || chapterNumber > 114) {
            // no next/previous token
            LOGGER.warn("No token found {}:{}:{}", chapterNumber, verseNumber, tokenNumber);
            return null;
        }
        if (verseNumber == -1) {
            // verse number "-1" indicates that this could be the last verse of this chapter and we don't know how
            // many verses are in this chapter, let's find out now
            verseNumber = repositoryUtil.getVerseCount(chapterNumber);
        }
        if (tokenNumber == -1) {
            // token number "-1" indicates that this could be the last token of this verse and we don't know how
            // many tokens are in this verse, let's find out now
            tokenNumber = repositoryUtil.getTokenCount(chapterNumber, verseNumber);
        }
        // at this stage if both verseNumber and tokenNumber are null, stop now
        if (verseNumber == -1 && tokenNumber == -1) {
            LOGGER.warn("No token found {}:{}:{}", chapterNumber, verseNumber, tokenNumber);
            return null;
        }
        Token dummy = new Token(chapterNumber, verseNumber, tokenNumber, "");
        LOGGER.debug("Finding token {}", dummy.getDisplayName());
        Token token = tokenRepository.findByDisplayName(dummy.getDisplayName());
        if (token == null) {
            if (next) {
                if (tokenNumber > 1) {
                    // we have situation where token number is greater then 0 and we still haven't found our token.
                    // The reference token should have been the last token of the verse, we have two possible cases:
                    // case 1: the reference token might have been the last token of the last verse of the chapter,
                    // in this case we need to go to the first token of first verse of next chapter
                    // case 2: the reference token might have been the last token of any verse other then last verse,
                    // in this case we need to go to the first token of the next verse while staying in the same chapter
                    // we are going to handle case 2 now
                    return getToken(chapterNumber, verseNumber + 1, 1, true, tokenRepository, repositoryUtil);
                } else if (verseNumber > 1) {
                    // handle case 1
                    return getToken(chapterNumber + 1, 1, 1, true, tokenRepository, repositoryUtil);
                }
            } else {
                if (verseNumber == 0) {
                    // handle case 3
                    return getToken(chapterNumber - 1, -1, -1, false, tokenRepository, repositoryUtil);
                } else if (tokenNumber == 0) {
                    // the reference token should have been the first token of verse, now there are two possible cases:
                    // case 3: the reference token might have been the first token of the first verse of the chapter,
                    // in this case we need to go to the last token of last verse of previous chapter
                    // case 4: the reference token might have been the first token of any verse other then first verse,
                    // in this case we need to go to the last token of the previous verse while staying in the same chapter
                    // we are going to handle case 4 now
                    // but we don't know the how many tokens in the previous verse, we are going to pass -1 as the token
                    // number
                    return getToken(chapterNumber, verseNumber - 1, -1, false, tokenRepository, repositoryUtil);
                }
            }
        }
        return token;
    }

    // Business methods

    public void createChapter(int chapterNumber) {
        if (verbose) {
            out.println(format("Start creating chapter {%s}", chapterNumber));
        }
        final Document document = tanzilTool.getChapter(chapterNumber, QURAN_SIMPLE_ENHANCED);
        com.alphasystem.tanzil.model.Chapter ch = document.getChapters().get(0);
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
                token.addLocation(location);
                verse.addToken(token);
                tokenNumber++;
            } // end of token loop
            verse.setTokenCount(verse.getTokens().size());
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
     * @return all chapters
     */
    public List<Chapter> findAllChapters() {
        List<Chapter> chapters = mongoTemplate.find(findAllChaptersQuery,
                Chapter.class, Chapter.class.getSimpleName().toLowerCase());
        sort(chapters, new ChapterComparator());
        return chapters;
    }

    public int getTokenCount(Integer chapterNumber, Integer verseNumber) {
        QVerse qVerse = QVerse.verse1;
        BooleanExpression predicate = qVerse.chapterNumber.eq(chapterNumber).and(qVerse.verseNumber.eq(verseNumber));
        Verse verse = verseRepository.findOne(predicate);
        return (verse == null) ? 0 : verse.getTokenCount();
    }

    public int getVerseCount(Integer chapterNumber) {
        BooleanExpression predicate = QChapter.chapter.chapterNumber.eq(chapterNumber);
        Chapter chapter = chapterRepository.findOne(predicate);
        return (chapter == null) ? 0 : chapter.getVerseCount();
    }

    public Token getNextToken(Token token) {
        LOGGER.debug("Getting next token for {}", token);
        if (token == null) {
            return null;
        }
        Token result = getToken(token.getChapterNumber(), token.getVerseNumber(), token.getTokenNumber() + 1, true,
                tokenRepository, this);
        LOGGER.debug("Next token for {} is {}", token, result);
        return result;
    }

    public Token getPreviousToken(Token token) {
        LOGGER.debug("Getting previous token for {}", token);
        if (token == null) {
            return null;
        }
        Token result = getToken(token.getChapterNumber(), token.getVerseNumber(), token.getTokenNumber() - 1, false,
                tokenRepository, this);
        LOGGER.debug("Previous token for {} is {}", token, result);
        return result;
    }

    public List<Token> getTokens(VerseTokenPairGroup group) {
        List<VerseTokensPair> pairs = group.getPairs();
        if (pairs == null || pairs.isEmpty()) {
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
        LOGGER.info(format("Group to find DependencyGraph is {%s}", group));
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

    public void saveDependencyGraph(DependencyGraph dependencyGraph, List<Token> impliedOrHiddenTokens,
                                    Map<GraphNodeType, List<String>> removalIds) {
        if (impliedOrHiddenTokens != null && !impliedOrHiddenTokens.isEmpty()) {
            impliedOrHiddenTokens.forEach(tokenRepository::save);
        }
        dependencyGraphRepository.save(dependencyGraph);
        if (removalIds != null && !removalIds.isEmpty()) {
            removalIds.entrySet().forEach(this::removeNode);
        }
    }

    public void deleteDependencyGraph(String id, Map<GraphNodeType, List<String>> removalIds) {
        if (!removalIds.isEmpty()) {
            removalIds.entrySet().forEach(this::removeNode);
        }
        DependencyGraph dependencyGraph = dependencyGraphRepository.findOne(id);
        VerseTokenPairGroup group = new VerseTokenPairGroup();
        group.setIncludeHidden(true);
        group.setChapterNumber(dependencyGraph.getChapterNumber());
        group.getPairs().addAll(dependencyGraph.getTokens());
        List<Token> hiddenTokens = getTokens(group);
        if (hiddenTokens != null && !hiddenTokens.isEmpty()) {
            hiddenTokens.forEach(token -> locationRepository.delete(token.getLocations()));
            tokenRepository.delete(hiddenTokens);
        }
        dependencyGraphRepository.delete(id);
    }

    @SuppressWarnings({"unchecked"})
    private void removeNode(Map.Entry<GraphNodeType, List<String>> entry) {
        GraphNodeType key = entry.getKey();
        List<String> ids = entry.getValue();
        GraphNodeRepository repository = getRepository(key);
        ids.forEach(repository::delete);
    }

    public DependencyGraph getDependencyGraph(String displayName) {
        return dependencyGraphRepository.findByDisplayName(displayName);
    }

    public MorphologicalEntry findMorphologicalEntry(MorphologicalEntry src) {
        src.initDisplayName();
        return morphologicalEntryRepository.findByDisplayName(src.getDisplayName());
    }

    public MorphologicalEntry findMorphologicalEntry(RootLetters src, NamedTemplate form) {
        return findMorphologicalEntry(new MorphologicalEntry(src, form));
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

    public MorphologicalEntryRepository getMorphologicalEntryRepository() {
        return morphologicalEntryRepository;
    }

    @Autowired
    public void setMorphologicalEntryRepository(MorphologicalEntryRepository morphologicalEntryRepository) {
        this.morphologicalEntryRepository = morphologicalEntryRepository;
    }

    public DictionaryNotesRepository getDictionaryNotesRepository() {
        return dictionaryNotesRepository;
    }

    @Autowired
    public void setDictionaryNotesRepository(DictionaryNotesRepository dictionaryNotesRepository) {
        this.dictionaryNotesRepository = dictionaryNotesRepository;
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
