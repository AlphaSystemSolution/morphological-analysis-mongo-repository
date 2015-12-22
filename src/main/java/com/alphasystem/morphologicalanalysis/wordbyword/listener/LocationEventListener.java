package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import static com.alphasystem.arabic.model.ArabicWord.getSubWord;

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
        TokenRepository tokenRepository = repositoryUtil.getTokenRepository();
        Token token = tokenRepository.findByChapterNumberAndVerseNumberAndTokenNumber(source.getChapterNumber(),
                source.getVerseNumber(), source.getTokenNumber());
        if (token == null) {
            logger.error("Token is null for location {}", source);
        }
        Integer startIndex = source.getStartIndex();
        Integer endIndex = source.getEndIndex();
        if (startIndex <= 0 && endIndex <= 0) {
            return;
        }
        if (token != null) {
            try {
                ArabicWord locationWord = getSubWord(token.getTokenWord(), startIndex, endIndex);
                source.setLocationWord(locationWord);
            } catch (Exception e) {
                logger.error("Error occurred while getting location word for token {} location {} @({}, {})",
                        token, source, startIndex, endIndex);
            }
        }
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Location> event) {
        super.onBeforeConvert(event);
        Location source = event.getSource();
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
