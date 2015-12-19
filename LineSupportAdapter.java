package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author sali
 */
public abstract class LineSupportAdapter<N extends LineSupport> extends GraphNodeAdapter<N> {

    /**
     * x1 location for line
     */
    private final DoubleProperty x1 = new SimpleDoubleProperty();

    /**
     * y1 location for line
     */
    private final DoubleProperty y1 = new SimpleDoubleProperty();

    /**
     * x2 location for line
     */
    private final DoubleProperty x2 = new SimpleDoubleProperty();

    /**
     * y2 location for line
     */
    private final DoubleProperty y2 = new SimpleDoubleProperty();

    protected LineSupportAdapter() {
        super();
        x1Property().addListener((observable, oldValue, newValue) -> getSrc().setX1((Double) newValue));
        y1Property().addListener((observable, oldValue, newValue) -> getSrc().setY1((Double) newValue));
        x2Property().addListener((observable, oldValue, newValue) -> getSrc().setX2((Double) newValue));
        y2Property().addListener((observable, oldValue, newValue) -> getSrc().setY2((Double) newValue));
    }

    @Override
    protected void initValues(N graphNode) {
        super.initValues(graphNode);
        setX1(graphNode == null ? null : graphNode.getX1());
        setY1(graphNode == null ? null : graphNode.getY1());
        setX2(graphNode == null ? null : graphNode.getX2());
        setY2(graphNode == null ? null : graphNode.getY2());

    }

    public final double getX1() {
        return x1.get();
    }

    public final void setX1(double x1) {
        this.x1.set(x1);
    }

    public final DoubleProperty x1Property() {
        return x1;
    }

    public final double getY1() {
        return y1.get();
    }

    public final void setY1(double y1) {
        this.y1.set(y1);
    }

    public final DoubleProperty y1Property() {
        return y1;
    }

    public final double getX2() {
        return x2.get();
    }

    public final void setX2(double x2) {
        this.x2.set(x2);
    }

    public final DoubleProperty x2Property() {
        return x2;
    }

    public final double getY2() {
        return y2.get();
    }

    public final void setY2(double y2) {
        this.y2.set(y2);
    }

    public final DoubleProperty y2Property() {
        return y2;
    }

}
