/**
 * 
 */
package com.alphasystem.morphologicalanalysis.util;

import java.util.Comparator;

import com.alphasystem.morphologicalanalysis.model.Verse;

/**
 * @author sali
 * 
 */
public class VerseComparator implements Comparator<Verse> {

	@Override
	public int compare(Verse o1, Verse o2) {
		int result = 0;
		if (o1 == null && o2 == null) {
			result = 0;
		} else if (o1 == null) {
			result = -1;
		} else if (o2 == null) {
			result = 1;
		} else {
			result = o1.getChapterNumber().compareTo(o2.getChapterNumber());
			if (result == 0) {
				Integer v1 = o1.getVerseNumber();
				Integer v2 = o2.getVerseNumber();
				result = v1.compareTo(v2);
			}
		}
		return result;
	}
}
