package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.REFERENCE;

/**
 * @author sali
 */
public class ReferenceNode extends TerminalNode {

    /**
     *
     */
    public ReferenceNode() {
        this(null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
    }

    /**
     * @param token
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
    public ReferenceNode(Token token, String id, Double x, Double y, Double x1, Double y1, Double x2,
                         Double y2, Double x3, Double y3) {
        this(token, id, x, y, x1, y1, x2, y2, x3, y3, 0d, 0d);
    }

    /**
     * @param token
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
    public ReferenceNode(Token token, String id, Double x, Double y, Double x1, Double y1, Double x2,
                         Double y2, Double x3, Double y3, Double translateX, Double translateY) {
        super(REFERENCE, token, id, x, y, x1, y1, x2, y2, x3, y3, translateX, translateY);
    }
}
