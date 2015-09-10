package com.alphasystem.morphologicalanalysis.graph.repository;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

import java.util.List;

/**
 * @author sali
 */
public interface DependencyGraphRepository extends BaseRepository<DependencyGraph> {

    /**
     * @param chapterNumber
     * @return
     */
    List<DependencyGraph> findByChapterNumber(Integer chapterNumber);

    /**
     *
     * @param chapterNumber
     * @param verseNumber
     * @param firstTokenIndex
     * @param lastTokenIndex
     * @return
     */
    DependencyGraph findByChapterNumberAndVerseNumberAndFirstTokenIndexAndLastTokenIndex(Integer chapterNumber,
                                                                                         Integer verseNumber,
                                                                                         Integer firstTokenIndex,
                                                                                         Integer lastTokenIndex);

    /**
     * @param chapterNumber
     * @param verseNumber
     * @return
     */
    Long countByChapterNumberAndVerseNumber(Integer chapterNumber, Integer verseNumber);
}
