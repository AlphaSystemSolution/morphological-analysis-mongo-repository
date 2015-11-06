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
}
