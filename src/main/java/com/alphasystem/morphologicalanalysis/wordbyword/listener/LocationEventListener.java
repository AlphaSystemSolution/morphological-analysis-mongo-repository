package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class LocationEventListener extends DocumentEventListener<Location> {

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Location> event) {
        super.onBeforeConvert(event);
        Location source = event.getSource();
        MorphologicalEntry morphologicalEntry = source.getMorphologicalEntry();
        if (morphologicalEntry != null) {
            if (morphologicalEntry.isEmpty()) {
                logger.debug("Trying to save location \"{}\" with empty morphologicalEntry \"{}\"", source, morphologicalEntry);
                source.setMorphologicalEntry(null);
            }
        }
    }
}
