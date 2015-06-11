package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author sali
 */
public abstract class LineSupport extends GraphNode {

    /**
     * x1 location for line
     */
    protected final DoubleProperty x1;

    /**
     * y1 location for line
     */
    protected final DoubleProperty y1;

    /**
     * x2 location for line
     */
    protected final DoubleProperty x2;

    /**
     * y2 location for line
     */
    protected final DoubleProperty y2;

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param translateX
     * @param translateY
     */
    protected LineSupport(GraphNodeType nodeType, String id, String text, Double x, Double y, Double x1,
                          Double y1, Double x2, Double y2, Double translateX, Double translateY) {
        super(nodeType, id, text, x, y, translateX, translateY);
        this.x1 = new SimpleDoubleProperty(x1);
        this.y1 = new SimpleDoubleProperty(y1);
        this.x2 = new SimpleDoubleProperty(x2);
        this.y2 = new SimpleDoubleProperty(y2);
    }

    public final Double getX1() {
        return x1.get();
    }

    public final void setX1(Double x1) {
        this.x1.set(x1);
    }

    public final DoubleProperty x1Property() {
        return x1;
    }

    public final Double getY1() {
        return y1.get();
    }

    public final void setY1(Double y1) {
        this.y1.set(y1);
    }

    public final DoubleProperty y1Property() {
        return y1;
    }

    public final Double getX2() {
        return x2.get();
    }

    public final void setX2(Double x2) {
        this.x2.set(x2);
    }

    public final DoubleProperty x2Property() {
        return x2;
    }

    public final Double getY2() {
        return y2.get();
    }

    public final void setY2(Double y2) {
        this.y2.set(y2);
    }

    public final DoubleProperty y2Property() {
        return y2;
    }
}
