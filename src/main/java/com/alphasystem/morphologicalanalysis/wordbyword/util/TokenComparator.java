/**
 * 
 */
package com.alphasystem.morphologicalanalysis.wordbyword.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

import java.util.Comparator;

/**
 * @author sali
 * 
 */
public class TokenComparator implements Comparator<Token> {

	@Override
	public int compare(Token o1, Token o2) {
		int result = 0;
		if (o1 == null && o2 == null) {
			result = 0;
		} else if (o1 == null) {
			result = -1;
		} else if (o2 == null) {
			result = 1;
		} else {
			result = o1.getVerseNumber().compareTo(o2.getVerseNumber());
			if (result == 0) {
				Integer tn1 = o1.getTokenNumber();
				Integer tn2 = o2.getTokenNumber();
				result = tn1.compareTo(tn2);
			}
		}
		return result;
	}

}
