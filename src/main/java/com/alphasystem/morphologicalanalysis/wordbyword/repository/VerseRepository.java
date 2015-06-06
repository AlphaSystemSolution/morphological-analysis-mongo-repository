/**
 * 
 */
package com.alphasystem.morphologicalanalysis.wordbyword.repository;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

import java.util.List;

/**
 * @author sali
 * 
 */
public interface VerseRepository extends BaseRepository<Verse> {

	/**
	 * @param chapterNumber
	 * @return
	 */
	List<Verse> findByChapterNumber(Integer chapterNumber);

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @return
	 */
	Verse findByChapterNumberAndVerseNumber(Integer chapterNumber,
											Integer verseNumber);
}
