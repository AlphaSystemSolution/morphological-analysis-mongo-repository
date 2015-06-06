/**
 * 
 */
package com.alphasystem.morphologicalanalysis.wordbyword.repository;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

import java.util.List;

/**
 * @author sali
 * 
 */
public interface LocationRepository extends BaseRepository<Location> {

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @param tokenNumber
	 * @return
	 */
	List<Location> findByChapterNumberAndVerseNumberAndTokenNumber(
			Integer chapterNumber, Integer verseNumber, Integer tokenNumber);

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @param tokenNumber
	 * @param locationNumber
	 * @return
	 */
	Location findByChapterNumberAndVerseNumberAndTokenNumberAndLocationNumber(
			Integer chapterNumber, Integer verseNumber, Integer tokenNumber,
			Integer locationNumber);
}
