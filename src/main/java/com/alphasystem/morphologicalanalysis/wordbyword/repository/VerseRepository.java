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

    /**
     * Returns {@link List} of verses for the given <code>chapter</code> for the given range.
     * <p>
     * <p>
     * <strong>NOTE:</strong> both <code>verseNumberFrom</code> and <code>verseNumberTo</code> are excluded.
     * </p>
     *
     * @param chapterNumber
     * @param verseNumberFrom
     * @param verseNumberTo
     * @return
     */
    List<Verse> findByChapterNumberAndVerseNumberBetween(Integer chapterNumber, Integer verseNumberFrom,
                                                         Integer verseNumberTo);
}
