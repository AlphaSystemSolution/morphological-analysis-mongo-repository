package com.alphasystem.morphologicalanalysis.graph.listener;

import com.alphasystem.morphologicalanalysis.graph.model.ReferenceNode;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class ReferenceNodeEventListener extends DocumentEventListener<ReferenceNode> {
}
