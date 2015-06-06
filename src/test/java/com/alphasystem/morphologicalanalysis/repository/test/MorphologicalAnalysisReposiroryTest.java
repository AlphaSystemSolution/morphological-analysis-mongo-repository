/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static com.alphasystem.persistence.mongo.spring.support.config.MongoConfig.MONGO_DB_NAME_PROPERTY;
import static com.alphasystem.util.Utils.getTimeConsumed;
import static java.lang.String.format;
import static java.lang.System.*;

/**
 * @author sali
 * 
 */
@ContextConfiguration(classes = { MorphologicalAnalysisSpringConfiguration.class })
public class MorphologicalAnalysisReposiroryTest extends AbstractTestNGSpringContextTests {

	static {
		setProperty(MONGO_DB_NAME_PROPERTY, "MORPHOLOGICAL_ANALYSIS_DB");
		setProperty("quranic-text", "quran-simple-enhanced");
		//-Dquranic-text=quran-simple-min -Dmongo.db.name=MORPHOLOGICAL_ANALYSIS_DB -Darabic-font-name="Traditional Arabic"
	}

	private MorphologicalAnalysisRepositoryUtil repositoryUtil;

	private MongoTemplate mongoTemplate;

	/**
	 * Creates initial data.
	 */
	@Test
	public void createChapters() {
		long startTime = currentTimeMillis();
		for (int chapterNumber = 1; chapterNumber <= 114; chapterNumber++) {
			long chapterStartTime = currentTimeMillis();
			repositoryUtil.createChapter(chapterNumber);
			long chapterEndTime = currentTimeMillis();
			out.printf("Time consume to save chapter {%s} is {%s}",
					chapterNumber, getTimeConsumed(chapterEndTime
							- chapterStartTime));
			out.println();
		}
		long endTime = currentTimeMillis();
		out.printf("Total time consume is {%s}", getTimeConsumed(endTime
				- startTime));
		out.println();
	}

	public void getAllChapters() {
		Query query = new Query();
		query.fields().include("chapterNumber").include("verseCount")
				.include("chapterName");
		List<Chapter> list = mongoTemplate.find(query, Chapter.class,
				Chapter.class.getSimpleName().toLowerCase());
		for (Chapter chapter : list) {
			out.println(format("%s:%s:%s", chapter.getChapterNumber(),
					chapter.getVerseCount(), chapter.getVerses().size()));
		}
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public MorphologicalAnalysisRepositoryUtil getRepositoryUtil() {
		return repositoryUtil;
	}

	@Autowired
	public void setRepositoryUtil(
			MorphologicalAnalysisRepositoryUtil repositoryUtil) {
		this.repositoryUtil = repositoryUtil;
	}

}
