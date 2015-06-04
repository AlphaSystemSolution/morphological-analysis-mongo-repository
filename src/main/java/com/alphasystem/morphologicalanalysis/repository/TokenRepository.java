/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

import java.util.List;

/**
 * @author sali
 * 
 */
public interface TokenRepository extends BaseRepository<Token> {

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @return
	 */
	List<Token> findByChapterNumberAndVerseNumber(Integer chapterNumber,
												  Integer verseNumber);

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @param tokenNumber
	 * @return
	 */
	Token findByChapterNumberAndVerseNumberAndTokenNumber(
			Integer chapterNumber, Integer verseNumber, Integer tokenNumber);
}
