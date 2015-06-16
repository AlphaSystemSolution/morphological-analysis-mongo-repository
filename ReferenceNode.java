package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.REFERENCE;

/**
 * @author sali
 */
public class ReferenceNode extends TerminalNode {

    private static final long serialVersionUID = 168512397691715688L;

    /**
     *
     */
    public ReferenceNode() {
        this(null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
    }

    /**
     * @param token
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public ReferenceNode(Token token, Double x, Double y, Double x1, Double y1, Double x2,
                         Double y2, Double x3, Double y3) {
        this(token, x, y, x1, y1, x2, y2, x3, y3, 0d, 0d);
    }

    /**
     * @param token
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
    public ReferenceNode(Token token, Double x, Double y, Double x1, Double y1, Double x2,
                         Double y2, Double x3, Double y3, Double translateX, Double translateY) {
        super(REFERENCE, token, x, y, x1, y1, x2, y2, x3, y3, translateX, translateY);
    }
}
