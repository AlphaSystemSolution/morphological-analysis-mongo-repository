package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
     * @param translateX
     * @param translateY
     * @param partOfSpeechNodes
     */
    public ReferenceNode(Token token, Double x, Double y, Double x1, Double y1, Double x2,
                         Double y2, Double x3, Double y3, Double translateX, Double translateY,
                         PartOfSpeechNode... partOfSpeechNodes) {
        super(REFERENCE, token, x, y, x1, y1, x2, y2, x3, y3, translateX, translateY, partOfSpeechNodes);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }
}
