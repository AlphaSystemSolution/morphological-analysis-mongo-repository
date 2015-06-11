package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import javafx.beans.property.*;

/**
 * @author sali
 */
public abstract class GraphNode {

    protected final StringProperty id;

    protected final ObjectProperty<GraphNodeType> nodeType;

    protected final StringProperty text;

    protected final DoubleProperty x;

    protected final DoubleProperty y;

    protected final DoubleProperty translateX;

    protected final DoubleProperty translateY;

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     */
    protected GraphNode(GraphNodeType nodeType, String id, String text, Double x, Double y) {
        this(nodeType, id, text, x, y, 0.0, 0.0);
    }

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     * @param translateX
     * @param translateY
     */
    protected GraphNode(GraphNodeType nodeType, String id, String text, Double x, Double y,
                        Double translateX, Double translateY) {
        this.id = new SimpleStringProperty(id);
        this.nodeType = new ReadOnlyObjectWrapper<>(nodeType);
        this.text = new SimpleStringProperty(text);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.translateX = new SimpleDoubleProperty(translateX);
        this.translateY = new SimpleDoubleProperty(translateY);
    }


    public final double getTranslateX() {
        return translateX.get();
    }

    public final void setTranslateX(double translateX) {
        this.translateX.set(translateX);
    }

    public final DoubleProperty translateXProperty() {
        return translateX;
    }

    public final double getTranslateY() {
        return translateY.get();
    }

    public final void setTranslateY(double translateY) {
        this.translateY.set(translateY);
    }

    public final DoubleProperty translateYProperty() {
        return translateY;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public final StringProperty idProperty() {
        return id;
    }

    public GraphNodeType getNodeType() {
        return nodeType.get();
    }

    public final ObjectProperty<GraphNodeType> nodeTypeProperty() {
        return nodeType;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public double getX() {
        return x.get();
    }

    public void setX(Double x) {
        this.x.set(x);
    }

    public final DoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public void setY(Double y) {
        this.y.set(y);
    }

    public final DoubleProperty yProperty() {
        return y;
    }


    @Override
    public String toString() {
        return getText();
    }
}
