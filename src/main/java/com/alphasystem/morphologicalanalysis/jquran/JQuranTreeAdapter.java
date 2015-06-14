/**
 * 
 */
package com.alphasystem.morphologicalanalysis.jquran;

/**
 * 
 */

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.tanzil.TanzilTool;
import com.alphasystem.tanzil.model.Verse;

import static com.alphasystem.arabic.model.ArabicLetterType.CLOSE_BRACKET;
import static com.alphasystem.arabic.model.ArabicLetterType.OPEN_BRACKET;
import static com.alphasystem.arabic.model.ArabicWord.*;

/**
 * @author sali
 * 
 */
public class JQuranTreeAdapter {

	private static TanzilTool tanzilTool = TanzilTool.getInstance();

	public static ArabicWord getVerse(int chapterNumber, int verseNumber) {
		Verse verse = tanzilTool.getVerse(chapterNumber, verseNumber);
		ArabicWord verseNumberWord = concatenate(getWord(CLOSE_BRACKET),
				getArabicNumber(verseNumber), getWord(OPEN_BRACKET));
		return concatenateWithSpace(verse.getVerse(), verseNumberWord);
	}

}
