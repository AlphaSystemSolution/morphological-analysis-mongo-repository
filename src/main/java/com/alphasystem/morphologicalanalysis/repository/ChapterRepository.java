/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository;

import com.alphasystem.morphologicalanalysis.model.Chapter;
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
	public Chapter findByChapterNumber(Integer chapterNumber);

}
