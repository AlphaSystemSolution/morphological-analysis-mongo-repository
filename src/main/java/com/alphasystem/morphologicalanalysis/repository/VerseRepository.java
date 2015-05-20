/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository;

import java.util.List;

import com.alphasystem.morphologicalanalysis.model.Verse;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

/**
 * @author sali
 * 
 */
public interface VerseRepository extends BaseRepository<Verse> {

	/**
	 * @param chapterNumber
	 * @return
	 */
	public List<Verse> findByChapterNumber(Integer chapterNumber);

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @return
	 */
	public Verse findByChapterNumberAndVerseNumber(Integer chapterNumber,
			Integer verseNumber);
}
