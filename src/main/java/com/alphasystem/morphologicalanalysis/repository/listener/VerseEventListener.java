/**
 * 
 */
package com.alphasystem.morphologicalanalysis.repository.listener;

import static com.alphasystem.morphologicalanalysis.jquran.JQuranTreeAdapter.getVerse;

import org.springframework.stereotype.Component;

import com.alphasystem.morphologicalanalysis.model.Verse;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import com.mongodb.DBObject;

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
