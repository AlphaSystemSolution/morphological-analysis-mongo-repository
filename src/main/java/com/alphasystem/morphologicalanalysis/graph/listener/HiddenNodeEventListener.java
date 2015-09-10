package com.alphasystem.morphologicalanalysis.graph.listener;

import com.alphasystem.morphologicalanalysis.graph.model.HiddenNode;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class HiddenNodeEventListener extends DocumentEventListener<HiddenNode> {
}
