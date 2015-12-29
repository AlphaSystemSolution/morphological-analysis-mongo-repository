/**
 *
 */
package com.alphasystem.morphologicalanalysis.wordbyword.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

import java.util.Comparator;

/**
 * @author sali
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
            result = o1.getChapterNumber().compareTo(o2.getChapterNumber());
            if (result == 0) {
                result = o1.getVerseNumber().compareTo(o2.getVerseNumber());
                if (result == 0) {
                    result = o1.getTokenNumber().compareTo(o2.getTokenNumber());
                }
            }
        }
        return result;
    }

}
