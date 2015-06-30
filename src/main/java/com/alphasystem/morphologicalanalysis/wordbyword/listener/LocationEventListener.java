package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void onAfterConvert(DBObject dbo, Location source) {
        super.onAfterConvert(dbo, source);

        TokenRepository tokenRepository = repositoryUtil.getTokenRepository();
        Token token = tokenRepository.findByChapterNumberAndVerseNumberAndTokenNumber(source.getChapterNumber(),
                source.getVerseNumber(), source.getTokenNumber());
        if(token == null){
            err.println(format("Token is null for location {%s}", source));
        }
        Integer startIndex = source.getStartIndex();
        Integer endIndex = source.getEndIndex();
        if(startIndex <= 0 && endIndex <= 0){
            return;
        }
        source.setLocationWord(getSubWord(token.getTokenWord(), startIndex, endIndex));
    }
}
