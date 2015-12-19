package com.alphasystem.morphologicalanalysis.tmp;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sali
 */
public class VerseTokenPairGroup {

    protected Integer chapterNumber;
    protected List<VerseTokensPair> pairs;

    public VerseTokenPairGroup() {
        setPairs(null);
    }

    public Integer getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(Integer chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public List<VerseTokensPair> getPairs() {
        return pairs;
    }

    public void setPairs(List<VerseTokensPair> pairs) {
        this.pairs = new ArrayList<>();
        if (pairs != null && !pairs.isEmpty()) {
            this.pairs.addAll(pairs);
        }
    }

    @Override
    public String toString() {
        if (pairs.isEmpty()) {
            return super.toString();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(pairs.get(0));
        for (int i = 1; i < pairs.size(); i++) {
            builder.append("|").append(pairs.get(i));
        }
        return builder.toString();
    }
}
