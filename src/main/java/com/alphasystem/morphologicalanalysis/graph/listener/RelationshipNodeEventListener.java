package com.alphasystem.morphologicalanalysis.graph.listener;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class RelationshipNodeEventListener extends DocumentEventListener<RelationshipNode> {
}
