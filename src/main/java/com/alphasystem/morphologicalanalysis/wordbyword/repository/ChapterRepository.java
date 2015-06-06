/**
 * 
 */
package com.alphasystem.morphologicalanalysis.wordbyword.repository;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

/**
 * @author sali
 * 
 */
public interface ChapterRepository extends BaseRepository<Chapter> {

	/**
	 * @param chapterNumber
	 * @return
	 */
	Chapter findByChapterNumber(Integer chapterNumber);

}
