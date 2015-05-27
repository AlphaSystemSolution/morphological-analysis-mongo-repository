/**
 * 
 */
package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.model.Chapter;
import com.alphasystem.morphologicalanalysis.model.Location;
import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.model.Verse;
import com.alphasystem.morphologicalanalysis.repository.ChapterRepository;
import com.alphasystem.morphologicalanalysis.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.repository.TokenRepository;
import com.alphasystem.morphologicalanalysis.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Collections.sort;
import static org.jqurantree.orthography.Document.getChapter;
import static org.jqurantree.orthography.Document.getVerse;

/**
 * @author sali
 * 
 */
@Component
public class MorphologicalAnalysisRepositoryUtil {

	private MongoTemplate mongoTemplate;

	private ChapterRepository chapterRepository;

	private VerseRepository verseRepository;

	private TokenRepository tokenRepository;

	private LocationRepository locationRepository;

	private Query findAllChaptersQuery;

	private boolean verbose;

	public MorphologicalAnalysisRepositoryUtil() {
		Query findAllChaptersQuery = new Query();
		findAllChaptersQuery.fields().include("chapterNumber")
				.include("verseCount").include("chapterName");
	}

	/**
	 * 
	 * @param chapterNumber
	 */
	public void createChapter(int chapterNumber) {
		if (verbose) {
			out.println(format("Start creating chapter {%s}", chapterNumber));
		}
		org.jqurantree.orthography.Chapter ch = getChapter(chapterNumber);
		Chapter chapter = new Chapter(chapterNumber, ch.getName().toUnicode());
		int verseCount = ch.getVerseCount();
		chapter.setVerseCount(verseCount);
		for (int verseNumber = 1; verseNumber <= verseCount; verseNumber++) {
			org.jqurantree.orthography.Verse jqVerse = getVerse(chapterNumber,
					verseNumber);
			if (verbose) {
				out.println(format("Start creating verse {%s}", verseNumber));
			}
			Verse verse = new Verse(chapterNumber, verseNumber);
			Iterable<org.jqurantree.orthography.Token> jqTokens = jqVerse
					.getTokens();
			int tokenNumber = 1;
			for (org.jqurantree.orthography.Token jqToken : jqTokens) {
				String tokenText = jqToken.toUnicode();
				Token token = new Token(chapterNumber, verseNumber,
						tokenNumber, tokenText);

				// we will create one location for each token
				Location location = new Location(chapterNumber, verseNumber,
						tokenNumber, 1);
				int tokenLength = tokenText.length();
				if (tokenLength <= 1) {
					// if token text length is exactly one then set start and
					// end index
					location.setStartIndex(0);
					location.setEndIndex(tokenLength);
				}
				locationRepository.save(location);
				token.addLocation(location);

				tokenNumber++;
				tokenRepository.save(token);
				verse.addToken(token);
			}// end of token loop
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

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
