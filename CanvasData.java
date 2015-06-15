package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    private final StringProperty id;
    private final ObjectProperty<CanvasMetaData> canvasMetaDataObject;
    private ObservableList<GraphNode> nodes = observableArrayList();
    private DependencyGraph dependencyGraph;

    public CanvasData(CanvasMetaData canvasMetaData) {
        id = new SimpleStringProperty();
        this.canvasMetaDataObject = new SimpleObjectProperty<>(canvasMetaData);
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public void setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
        if (this.dependencyGraph != null) {
            setId(this.dependencyGraph.getId());
        }
    }

    public final String getId() {
        return id.get();
    }

    public final void setId(String id) {
        this.id.set(id);
    }

    public final StringProperty idProperty() {
        return id;
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
