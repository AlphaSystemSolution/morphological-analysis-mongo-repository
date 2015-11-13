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

import static com.alphasystem.arabic.model.ArabicLetterType.ORNATE_LEFT_PARENTHESIS;
import static com.alphasystem.arabic.model.ArabicLetterType.ORNATE_RIGHT_PARENTHESIS;
import static com.alphasystem.arabic.model.ArabicWord.*;
import static com.alphasystem.tanzil.QuranScript.QURAN_SIMPLE_ENHANCED;

/**
 * @author sali
 * 
 */
public class JQuranTreeAdapter {

	private static TanzilTool tanzilTool = TanzilTool.getInstance();

	public static ArabicWord getVerse(int chapterNumber, int verseNumber) {
		Verse verse = tanzilTool.getVerse(chapterNumber, verseNumber, QURAN_SIMPLE_ENHANCED);
		ArabicWord verseNumberWord = concatenate(getWord(ORNATE_RIGHT_PARENTHESIS),
				getArabicNumber(verseNumber), getWord(ORNATE_LEFT_PARENTHESIS));
		return concatenateWithSpace(verse.getVerse(), verseNumberWord);
	}

}
