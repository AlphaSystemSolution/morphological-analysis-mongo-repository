package com.alphasystem.morphologicalanalysis.morphology.listener;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class MorphologicalEntryEventListener extends DocumentEventListener<MorphologicalEntry> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<MorphologicalEntry> event) {
        super.onBeforeConvert(event);
        MorphologicalEntry source = event.getSource();
        // if at this point either of root letters or form is null then raise the flag
        if (source.isEmpty()) {
            throw new RuntimeException("RootLetters and/or form cannot be null.");
        }
    }
}
