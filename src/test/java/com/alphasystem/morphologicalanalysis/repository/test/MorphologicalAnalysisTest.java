package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.common.model.Related;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.graph.model.Terminal;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.RelationshipRepository;
import com.alphasystem.morphologicalanalysis.spring.support.GraphConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.spring.support.WordByWordConfig;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType.MASCULINE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus.GENETIVE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounType.DEFINITE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType.PLURAL;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType.SINGULAR;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.DEFINITE_ARTICLE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType.MUDAF_ILAIH;
import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
        MorphologicalAnalysisSpringConfiguration.class})
public class MorphologicalAnalysisTest extends AbstractTestNGSpringContextTests {

    private static final int DEFAULT_CHAPTER_NUMBER = 1;
    private static final int DEFAULT_VERSE_NUMBER = 2;

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @BeforeClass
    public void beforeSuite(){
        repositoryUtil.getMongoTemplate().getDb().dropDatabase();
    }

    @Test
    public void createChapter() {
        log(format("Creating chapter: %s", DEFAULT_CHAPTER_NUMBER), true);
        repositoryUtil.createChapter(DEFAULT_CHAPTER_NUMBER);
        log(format("Chapter %s created", DEFAULT_CHAPTER_NUMBER), true);

        Verse verse = repositoryUtil.getVerseRepository().
                findByChapterNumberAndVerseNumber(DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER);
        assertEquals(verse.getTokenCount(), new Integer(4));
    }

    @Test(dependsOnMethods = "createChapter")
    public void populateToken4() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 4);
        assertNotNull(token);
        int length = token.getTokenWord().getLength();
        log(format("Token word length: %s", length), true);
        Location location = token.getLocations().get(0);
        location.setPartOfSpeech(DEFINITE_ARTICLE);
        location.setStartIndex(0);
        location.setEndIndex(2);
        repositoryUtil.getLocationRepository().save(location);

        location = new Location(DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, token.getTokenNumber(), 2)
                .withStartIndex(2).withEndIndex(length);
        NounProperties properties = (NounProperties) location.getProperties();
        properties.setNounType(DEFINITE);
        properties.setGender(MASCULINE);
        properties.setNumber(PLURAL);
        properties.setStatus(GENETIVE);
        repositoryUtil.getLocationRepository().save(location);
        token.getLocations().add(location);
        repositoryUtil.getTokenRepository().save(token);
    }

    @Test(dependsOnMethods = "populateToken4")
    public void populateToken3() {
        Token token = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumberAndTokenNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 3);
        assertNotNull(token);
        int length = token.getTokenWord().getLength();
        log(format("Token word length: %s", length), true);
        Location location = token.getLocations().get(0);
        NounProperties properties = (NounProperties) location.getProperties();
        properties.setNounType(DEFINITE);
        properties.setGender(MASCULINE);
        properties.setNumber(SINGULAR);
        properties.setStatus(GENETIVE);
        repositoryUtil.getLocationRepository().save(location);
    }

    @Test(dependsOnMethods = "populateToken3")
    public void createDependencyGraph() {
        DependencyGraphRepository repository = repositoryUtil.getDependencyGraphRepository();
        Long count = repository.countByChapterNumberAndVerseNumber(DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER);
        log(format("Count for chapter number %s and verse number %s is : %s",
                DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, count), true);
        int segmentNumber = (int) (count + 1);
        List<Token> tokens = repositoryUtil.getTokenRepository().findByChapterNumberAndVerseNumber(
                DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER);
        Token firstToken = tokens.get(0);
        Token lastToken = tokens.get(tokens.size() - 1);
        DependencyGraph dependencyGraph = new DependencyGraph(DEFAULT_CHAPTER_NUMBER,
                DEFAULT_VERSE_NUMBER, firstToken.getTokenNumber(), lastToken.getTokenNumber());

        List<Terminal> terminals = new ArrayList<>();
        for (Token token : tokens) {
            terminals.add(new Terminal(token));
        }

        dependencyGraph.setTerminals(terminals);
        dependencyGraph.getRelationships().add(createRelationship());

        repositoryUtil.getDependencyGraphRepository().save(dependencyGraph);
    }

    private Relationship createRelationship() {
        Relationship relationship = new Relationship();

        LocationRepository repository = repositoryUtil.getLocationRepository();
        Location location = repository.findByChapterNumberAndVerseNumberAndTokenNumberAndLocationNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 4, 2);
        assertNotNull(location);
        relationship.setDependent(location);

        location = repository.findByChapterNumberAndVerseNumberAndTokenNumberAndLocationNumber
                (DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 3, 1);
        assertNotNull(location);
        relationship.setOwner(location);
        relationship.setRelationship(MUDAF_ILAIH);
        return relationship;
    }

    @Test(dependsOnMethods = "createDependencyGraph")
    public void checkDependencyGraph() {
        List<DependencyGraph> list = repositoryUtil.getDependencyGraphRepository().findByChapterNumberAndVerseNumber(
                DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER);
        assertNotNull(list);
        assertEquals(list.size(), 1);
        DependencyGraph dg = list.get(0);
        List<Terminal> terminals = dg.getTerminals();
        assertNotNull(terminals);
        assertEquals(!terminals.isEmpty(), true);
        terminals.forEach(terminal -> System.out.println("token = " + terminal.getToken()));
    }

    @Test(dependsOnMethods = "checkDependencyGraph")
    public void createChapter2() {
        int chapterNumber = 2;
        log(format("Creating chapter %s", chapterNumber), true);
        repositoryUtil.createChapter(chapterNumber);
        log(format("Created chapter %s", chapterNumber), true);
    }

    @Test(dependsOnMethods = "createChapter2")
    public void getRangeOfVerses() {
        int chapterNumber = 2;
        int from = 10;
        int to = 21;
        int len = to - from - 1;
        VerseRepository verseRepository = repositoryUtil.getVerseRepository();
        List<Verse> verses = verseRepository.findByChapterNumberAndVerseNumberBetween(chapterNumber, from, to);
        assertNotNull(verses);
        assertEquals(verses.isEmpty(), false);
        assertEquals(verses.size(), len);
        for (Verse verse : verses) {
            log(format("VERSE: %s", verse.getDisplayName()), true);
        }
    }

    @Test(dependsOnMethods = "getRangeOfVerses")
    public void loadLocation() {
        LocationRepository locationRepository = repositoryUtil.getLocationRepository();
        Location location = locationRepository.findByDisplayName(format("%s:%s:%s:%s",
                DEFAULT_CHAPTER_NUMBER, DEFAULT_VERSE_NUMBER, 4, 2));
        log(format("Location found: %s (%s)", location, location.getLocationWord().toBuckWalter()), true);
    }

    @Test(dependsOnMethods = "loadLocation")
    public void loadRelationship() {
        RelationshipRepository relationshipRepository = repositoryUtil.getRelationshipRepository();
        Relationship relationship = relationshipRepository.findByDisplayName("1:2:4:2::1:2:3:1");
        Related owner = relationship.getOwner();
        Class<? extends Related> aClass = owner.getClass();
        log(aClass.getName(), true);
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            log(method.getName(), true);
        }
    }

}
