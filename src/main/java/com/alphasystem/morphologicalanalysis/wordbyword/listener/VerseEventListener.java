/**
 * 
 */
package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;

import static com.alphasystem.morphologicalanalysis.jquran.JQuranTreeAdapter.getVerse;

/**
 * @author sali
 * 
 */
@Component
public class VerseEventListener extends DocumentEventListener<Verse> {

	@Override
	public void onAfterConvert(DBObject dbo, Verse source) {
		super.onAfterConvert(dbo, source);
		source.setVerse(getVerse(source.getChapterNumber(),
				source.getVerseNumber()));
	}
}
