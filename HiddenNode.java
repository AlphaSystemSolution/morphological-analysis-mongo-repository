package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.arabic.model.ArabicWord;

import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.HIDDEN;

/**
 * @author sali
 */
public class HiddenNode extends TerminalNode {

    private static final long serialVersionUID = 6414341448851608265L;

    /**
     *
     */
    public HiddenNode() {
        this(WORD_SPACE, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
    }

    /**
     * @param arabicWord
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public HiddenNode(ArabicWord arabicWord, String id, Double x, Double y, Double x1, Double y1, Double x2,
                      Double y2, Double x3, Double y3) {
        this(arabicWord, id, x, y, x1, y1, x2, y2, x3, y3, 0d, 0d);
    }

    /**
     * @param arabicWord
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param translateX
     * @param translateY
     */
    public HiddenNode(ArabicWord arabicWord, String id, Double x, Double y, Double x1, Double y1, Double x2,
                      Double y2, Double x3, Double y3, Double translateX, Double translateY) {
        super(HIDDEN, id, arabicWord.toUnicode(), x, y, x1, y1, x2, y2, x3, y3, translateX, translateY);
    }
}
