package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class DependencyGraphAdapter {

    private final ObjectProperty<DependencyGraph> dependencyGraph = new SimpleObjectProperty<>();
    private final ObjectProperty<GraphMetaInfoAdapter> graphMetaInfo = new SimpleObjectProperty<>();
    private final ObservableList<GraphNodeAdapter> graphNodes = observableArrayList();
    private final ObjectProperty<GraphNodeAdapter> selectedNode = new SimpleObjectProperty<>();

    public DependencyGraphAdapter(DependencyGraph src) {
        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                setGraphMetaInfo(new GraphMetaInfoAdapter(new GraphMetaInfo()));
                getGraphNodes().remove(0, getGraphNodes().size());
                return;
            }
            setGraphMetaInfo(new GraphMetaInfoAdapter(newValue.getMetaInfo()));
            newValue.getNodes().forEach(graphNode -> {
                GraphNodeAdapter adapter = null;
                GraphNodeType graphNodeType = graphNode.getGraphNodeType();
                switch (graphNodeType) {
                    case TERMINAL:
                        adapter = new TerminalNodeAdapter();
                        break;
                    case PHRASE:
                        adapter = new PhraseNodeAdapter();
                        break;
                    case RELATIONSHIP:
                        adapter = new RelationshipNodeAdapter();
                        break;
                    case REFERENCE:
                        adapter = new ReferenceNodeAdapter();
                        break;
                    case HIDDEN:
                        adapter = new HiddenNodeAdapter();
                        break;
                    case IMPLIED:
                        adapter = new ImpliedNodeAdapter();
                        break;
                    default:
                        break;
                }
                if (adapter != null) {
                    adapter.setSrc(graphNode);
                    switch (graphNodeType) {
                        case RELATIONSHIP:
                            updateRelationshipDependentAndOwner((RelationshipNodeAdapter) adapter);
                            break;
                        case PHRASE:
                            updatePhraseNodeFragments((PhraseNodeAdapter) adapter);
                            break;
                    }
                    getGraphNodes().add(adapter);
                }
            });
        });
        setDependencyGraph(src);
    }

    private void updateRelationshipDependentAndOwner(RelationshipNodeAdapter rn) {
        LinkSupportAdapter dependent = getActualAdapter(rn.getDependent());
        rn.setDependent(null);
        rn.setDependent(dependent);

        LinkSupportAdapter owner = getActualAdapter(rn.getOwner());
        rn.setOwner(null);
        rn.setOwner(owner);
    }

    private void updatePhraseNodeFragments(PhraseNodeAdapter adapter) {
        ObservableList<PartOfSpeechNodeAdapter> fragments = adapter.getFragments();
        ObservableList<PartOfSpeechNodeAdapter> actualFragments = observableArrayList();
        actualFragments.addAll(fragments.stream().map(fragment -> (PartOfSpeechNodeAdapter) getActualAdapter(fragment))
                .collect(Collectors.toList()));
        adapter.getFragments().setAll(actualFragments);
    }

    private LinkSupportAdapter getActualAdapter(LinkSupportAdapter src) {
        LinkSupportAdapter actual = null;
        outer:
        for (GraphNodeAdapter node : graphNodes) {
            GraphNodeType graphNodeType = node.getGraphNodeType();
            String sourceId = src.getSrc().getId();
            switch (graphNodeType) {
                case TERMINAL:
                case REFERENCE:
                case IMPLIED:
                case HIDDEN:
                    TerminalNodeAdapter tn = (TerminalNodeAdapter) node;
                    for (PartOfSpeechNodeAdapter pn : tn.getPartOfSpeeches()) {
                        if (pn.getSrc().getId().equals(sourceId)) {
                            actual = pn;
                            break outer;
                        }
                    }
                    break;
                case PHRASE:
                    PhraseNodeAdapter pn = (PhraseNodeAdapter) node;
                    if (pn.getSrc().getId().equals(sourceId)) {
                        actual = pn;
                        break outer;
                    }
                    break;
            }
        }
        return actual;
    }

    public boolean isEmpty() {
        return graphNodes.isEmpty();
    }

    public final GraphNodeAdapter getSelectedNode() {
        return selectedNode.get();
    }

    public final void setSelectedNode(GraphNodeAdapter selectedNode) {
        this.selectedNode.set(selectedNode);
    }

    public final ObjectProperty<GraphNodeAdapter> selectedNodeProperty() {
        return selectedNode;
    }

    public final DependencyGraph getDependencyGraph() {
        return dependencyGraph.get();
    }

    public final void setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph.set(dependencyGraph);
    }

    public final ObjectProperty<DependencyGraph> dependencyGraphProperty() {
        return dependencyGraph;
    }

    public final GraphMetaInfoAdapter getGraphMetaInfo() {
        return graphMetaInfo.get();
    }

    public final void setGraphMetaInfo(GraphMetaInfoAdapter graphMetaInfo) {
        this.graphMetaInfo.set(graphMetaInfo);
    }

    public final ObjectProperty<GraphMetaInfoAdapter> graphMetaInfoProperty() {
        return graphMetaInfo;
    }

    public final ObservableList<GraphNodeAdapter> getGraphNodes() {
        return graphNodes;
    }
}
