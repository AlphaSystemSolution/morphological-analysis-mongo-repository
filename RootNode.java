package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.ROOT;

/**
 * @author sali
 */
public class RootNode extends GraphNode {

    private static final long serialVersionUID = 7452723324237575111L;

    private GraphNodeType childType;

    public RootNode() {
        this(ROOT);
    }

    /**
     * @param childType
     */
    public RootNode(GraphNodeType childType) {
        super(ROOT, "dummy", null, -1.0, -1.0, 0.0, 0.0);
        setChildType(childType);
    }

    public GraphNodeType getChildType() {
        return childType;
    }

    private void setChildType(GraphNodeType childType) {
        this.childType = childType;
        if (this.childType != null) {
            setText(childType.name());
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(getChildType());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setChildType((GraphNodeType) in.readObject());
    }

    @Override
    public String toString() {
        return getText();
    }
}
