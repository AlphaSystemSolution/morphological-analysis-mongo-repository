package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.EMPTY;

/**
 * @author sali
 */
public class EmptyNode extends TerminalNode {

    /**
     * Default Constructor.
     */
    public EmptyNode() {
        this(null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, null);
    }

    /**
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param partOfSpeechNode
     */
    public EmptyNode(String id, Double x, Double y, Double x1, Double y1, Double x2,
                     Double y2, Double x3, Double y3, PartOfSpeechNode partOfSpeechNode) {
        this(id, x, y, x1, y1, x2, y2, x3, y3, 0d, 0d, partOfSpeechNode);
    }

    /**
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
     * @param partOfSpeechNode
     */
    public EmptyNode(String id, Double x, Double y, Double x1, Double y1, Double x2,
                     Double y2, Double x3, Double y3, Double translateX, Double translateY,
                     PartOfSpeechNode partOfSpeechNode) {
        super(EMPTY, id, "(*)", x, y, x1, y1, x2, y2, x3, y3, translateX, translateY);
        getPartOfSpeeches().setAll(partOfSpeechNode);
    }
}
