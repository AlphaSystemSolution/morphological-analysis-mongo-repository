package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class CanvasData {

    private final ObjectProperty<CanvasMetaData> canvasMetaDataObject;
    private ObservableList<GraphNode> nodes = observableArrayList();

    public CanvasData(CanvasMetaData canvasMetaData) {
        this.canvasMetaDataObject = new SimpleObjectProperty<>(canvasMetaData);
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
}
