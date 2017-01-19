package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
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
    public void onAfterConvert(AfterConvertEvent<Location> event) {
        super.onAfterConvert(event);

        Location source = event.getSource();
        final String text = source.getText();
        if (StringUtils.isNotBlank(text)) {
            return;
        }

        ArabicWord locationWord = repositoryUtil.getLocationWord(source);
        if (locationWord != null) {
            logger.info("Setting text for \"{}\" for location \"{}\"", locationWord.toBuckWalter(), source.getDisplayName());
            source.setText(locationWord.toUnicode());
        }

    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Location> event) {
        super.onBeforeConvert(event);
        Location source = event.getSource();
        String text = source.getText();
        if (StringUtils.isBlank(text)) {
            ArabicWord locationWord = null;
            String errorMessage = null;
            try {
                locationWord = repositoryUtil.getLocationWord(source);
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            if (locationWord == null) {
                logger.warn("Error retrieving location text for location \"{}\", error message was \"{}\"", source.getDisplayName(), errorMessage);
            } else {
                logger.info("Setting text for location", locationWord.toUnicode());
                source.setText(locationWord.toUnicode());
            }
        }
        MorphologicalEntry morphologicalEntry = source.getMorphologicalEntry();
        if (morphologicalEntry != null) {
            if (morphologicalEntry.isEmpty()) {
                logger.warn("Trying to save location \"{}\" with empty morphologicalEntry \"{}\"", source, morphologicalEntry);
                source.setMorphologicalEntry(null);
            } else {
                morphologicalEntry.getLocations().add(source);
            }
        }
    }
}
