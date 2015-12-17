package com.alphasystem.morphologicalanalysis.morphology.listener;

import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class RootLettersEventListener extends DocumentEventListener<RootLetters> {
}
