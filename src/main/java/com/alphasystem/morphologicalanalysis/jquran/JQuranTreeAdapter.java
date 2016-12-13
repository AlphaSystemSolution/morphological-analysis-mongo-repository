/**
 *
 */
package com.alphasystem.morphologicalanalysis.jquran;

/**
 *
 */

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.tanzil.TanzilTool;
import com.alphasystem.tanzil.model.Chapter;
import com.alphasystem.tanzil.model.Document;
import com.alphasystem.tanzil.model.Verse;

import static com.alphasystem.arabic.model.ArabicTool.getVerseChapterNumber;
import static com.alphasystem.arabic.model.ArabicWord.concatenateWithSpace;
import static com.alphasystem.tanzil.QuranScript.QURAN_SIMPLE_ENHANCED;

/**
 * @author sali
 */
public class JQuranTreeAdapter {

    private static TanzilTool tanzilTool = TanzilTool.getInstance();

    public static ArabicWord getVerse(int chapterNumber, int verseNumber) {
        final Document document = tanzilTool.getVerse(chapterNumber, verseNumber, QURAN_SIMPLE_ENHANCED);
        Chapter chapter = document.getChapters().get(0);
        ArabicWord verseNumberWord = getVerseChapterNumber(chapterNumber, verseNumber, false, true, true);
        Verse verse = chapter.getVerses().get(0);
        return concatenateWithSpace(verse.getVerse(), verseNumberWord);
    }

}
