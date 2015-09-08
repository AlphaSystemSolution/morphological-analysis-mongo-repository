package com.alphasystem.morphologicalanalysis.graph.repository;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.persistence.mongo.repository.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author sali
 */
@NoRepositoryBean
public interface GraphNodeRepository<N extends GraphNode> extends BaseRepository<N> {

}
