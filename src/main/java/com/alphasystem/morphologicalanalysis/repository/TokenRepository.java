/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository;

import java.util.List;

import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

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
	public List<Token> findByChapterNumberAndVerseNumber(Integer chapterNumber,
			Integer verseNumber);

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 * @param tokenNumber
	 * @return
	 */
	public Token findByChapterNumberAndVerseNumberAndTokenNumber(
			Integer chapterNumber, Integer verseNumber, Integer tokenNumber);
}
