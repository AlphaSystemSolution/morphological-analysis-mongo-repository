package com.alphasystem.morphologicalanalysis.graph.repository;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author sali
 */
@NoRepositoryBean
public interface AbstractTerminalNodeRepository<N extends TerminalNode> extends GraphNodeRepository<N> {

    /**
     *
     * @param chapterNumber
     * @param verseNumber
     * @param tokenNumber
     * @return
     */
    Long countByChapterNumberAndVerseNumberAndTokenNumber(Integer chapterNumber, Integer verseNumber,
                                                          Integer tokenNumber);
}
