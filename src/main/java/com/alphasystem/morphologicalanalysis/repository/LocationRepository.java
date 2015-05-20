/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository;

import java.util.List;

import com.alphasystem.morphologicalanalysis.model.Location;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

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
	public List<Location> findByChapterNumberAndVerseNumberAndTokenNumber(
			Integer chapterNumber, Integer verseNumber, Integer tokenNumber);

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @param tokenNumber
	 * @param locationNumber
	 * @return
	 */
	public Location findByChapterNumberAndVerseNumberAndTokenNumberAndLocationNumber(
			Integer chapterNumber, Integer verseNumber, Integer tokenNumber,
			Integer locationNumber);
}
