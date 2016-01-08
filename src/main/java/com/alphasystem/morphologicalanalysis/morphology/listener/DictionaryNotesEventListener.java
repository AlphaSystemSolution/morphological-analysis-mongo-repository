package com.alphasystem.morphologicalanalysis.morphology.listener;

import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * @author sali
 */
@Component
public class DictionaryNotesEventListener extends DocumentEventListener<DictionaryNotes> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<DictionaryNotes> event) {
        super.onBeforeConvert(event);

        DictionaryNotes source = event.getSource();
        RootLetters rootLetters = source.getRootLetters();
        if (rootLetters == null || rootLetters.isEmpty()) {
            logger.warn("Attempt to save DictionaryNotes \"{}\" with null or empty root letters", source.getId());
            throw new RuntimeException(format("Attempt to save DictionaryNotes \"%s\" with null or empty root letters", source.getId()));
        }
    }
}
