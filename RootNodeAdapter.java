package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.RootNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;

/**
 * @author sali
 */
public class RootNodeAdapter extends GraphNodeAdapter<RootNode> {

    public RootNodeAdapter(GraphNodeType childNodeType) {
        super();
        setSrc(new RootNode(childNodeType));
    }

}
