package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.ROOT;

/**
 * @author sali
 */
public class RootNode extends GraphNode {

    private final GraphNodeType childType;

    /**
     *
     * @param childType
     */
    public RootNode(GraphNodeType childType) {
        super(ROOT, "dummy", childType.name(), -1.0, -1.0, 0.0, 0.0);
        this.childType = childType;

    }

    public GraphNodeType getChildType() {
        return childType;
    }

    @Override
    public String toString() {
        return getText();
    }
}
