package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.stereotype.Component;

import static com.alphasystem.arabic.model.ArabicWord.getSubWord;
import static java.lang.String.format;
import static java.lang.System.err;

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
            err.println(format("Token is null for location {%s}", source));
        }
        Integer startIndex = source.getStartIndex();
        Integer endIndex = source.getEndIndex();
        if (startIndex <= 0 && endIndex <= 0) {
            return;
        }
        ArabicWord locationWord = getSubWord(token.getTokenWord(), startIndex, endIndex);
        source.setLocationWord(locationWord);
    }

}
