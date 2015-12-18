package com.alphasystem.morphologicalanalysis.morphology.listener;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalGroup;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class MorphologicalGroupEventListener extends DocumentEventListener<MorphologicalGroup> {
}
