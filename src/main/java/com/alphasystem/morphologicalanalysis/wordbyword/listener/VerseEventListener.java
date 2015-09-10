/**
 * 
 */
package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.stereotype.Component;

import static com.alphasystem.morphologicalanalysis.jquran.JQuranTreeAdapter.getVerse;

/**
 * @author sali
 * 
 */
@Component
public class VerseEventListener extends DocumentEventListener<Verse> {

	@Override
	public void onAfterConvert(AfterConvertEvent<Verse> event) {
		super.onAfterConvert(event);
		Verse source = event.getSource();
		source.setVerse(getVerse(source.getChapterNumber(),
				source.getVerseNumber()));
	}

}
