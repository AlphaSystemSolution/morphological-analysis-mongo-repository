/**
 * 
 */
package com.alphasystem.morphologicalanalysis.jquran;

/**
 * 
 */

import static com.alphasystem.arabic.model.ArabicLetterType.CLOSE_BRACKET;
import static com.alphasystem.arabic.model.ArabicLetterType.OPEN_BRACKET;
import static com.alphasystem.arabic.model.ArabicWord.concatenate;
import static com.alphasystem.arabic.model.ArabicWord.concatenateWithSpace;
import static com.alphasystem.arabic.model.ArabicWord.fromBuckWalterString;
import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.arabic.model.ArabicWord.getWord;

import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Verse;

import com.alphasystem.arabic.model.ArabicWord;

/**
 * @author sali
 * 
 */
public class JQuranTreeAdapter {

	public static ArabicWord getVerse(int chapterNumber, int verseNumber) {
		Verse verse = Document.getVerse(chapterNumber, verseNumber);
		ArabicWord verseNumberWord = concatenate(getWord(CLOSE_BRACKET),
				getArabicNumber(verseNumber), getWord(OPEN_BRACKET));
		return concatenateWithSpace(fromBuckWalterString(verse.toBuckwalter()),
				verseNumberWord);
	}

}
