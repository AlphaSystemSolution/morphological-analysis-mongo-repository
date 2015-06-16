package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class CanvasData implements Externalizable {

    private static final long serialVersionUID = 7648139043189886298L;

    private final ReadOnlyStringWrapper id;
    private final ObjectProperty<CanvasMetaData> canvasMetaDataObject;
    private final ObjectProperty<DependencyGraph> dependencyGraph;
    private ObservableList<GraphNode> nodes = observableArrayList();

    public CanvasData() {
        this(new CanvasMetaData());
    }

    public CanvasData(CanvasMetaData canvasMetaData) {
        id = new ReadOnlyStringWrapper();
        dependencyGraph = new SimpleObjectProperty<>();
        this.canvasMetaDataObject = new SimpleObjectProperty<>(canvasMetaData);
        dependencyGraph.addListener((observable, oldValue, newValue) -> {
            String id = newValue == null ? null : newValue.getId();
            setId(id);
        });
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

    public final String getId() {
        return id.get();
    }

    public final void setId(String id) {
        this.id.set(id);
    }

    public final ReadOnlyStringProperty idProperty() {
        return id.getReadOnlyProperty();
    }

    public boolean add(GraphNode graphNode) {
        return nodes.add(graphNode);
    }

    public void add(int index, GraphNode element) {
        nodes.add(index, element);
    }

    public GraphNode get(int index) {
        return nodes.get(index);
    }

    public GraphNode remove(int index) {
        return nodes.remove(index);
    }

    public ObservableList<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(ObservableList<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public CanvasMetaData getCanvasMetaData() {
        return canvasMetaDataObject.get();
    }

    public void setCanvasMetaData(CanvasMetaData canvasMetaDataObject) {
        this.canvasMetaDataObject.set(canvasMetaDataObject);
    }

    public final ObjectProperty<CanvasMetaData> canvasMetaDataObjectProperty() {
        return canvasMetaDataObject;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getId());
        out.writeObject(getCanvasMetaData());
        List<GraphNode> nodes = new ArrayList<>();
        if (this.nodes != null && !this.nodes.isEmpty()) {
            nodes.addAll(this.nodes);
        }
        out.writeObject(nodes);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setId((String) in.readObject());
        setCanvasMetaData((CanvasMetaData) in.readObject());
        this.nodes.setAll((List<GraphNode>) in.readObject());
    }
}
