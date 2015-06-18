package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A {@link LinkSupport} class represents nodes that cab be linked to other nodes by a relationship. Each node of this
 * type will have a circle drawn below it which will be linked to other {@link LinkSupport} nodes. This class contains
 * x and y coordinates of that circle.
 *
 * @author sali
 */
public abstract class LinkSupport extends LineSupport {

    private static final long serialVersionUID = -9097341823128901056L;

    /**
     * x location for circle.
     */
    protected final DoubleProperty cx;

    /**
     * y location for circle
     */
    protected final DoubleProperty cy;

    protected LinkSupport() {
        this(null, null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);

    }

    protected LinkSupport(GraphNodeType nodeType, String id, String text, Double x, Double y,
                          Double cx, Double cy) {
        this(nodeType, id, text, x, y, 0d, 0d, 0d, 0d, 0d, 0d, cx, cy);

    }

    protected LinkSupport(GraphNodeType nodeType, String id, String text, Double x, Double y, Double x1, Double y1,
                          Double x2, Double y2, Double translateX, Double translateY, Double cx, Double cy) {
        super(nodeType, id, text, x, y, x1, y1, x2, y2, translateX, translateY);
        this.cx = new SimpleDoubleProperty(cx);
        this.cy = new SimpleDoubleProperty(cy);
    }

    public final double getCx() {
        return cx.get();
    }

    public final void setCx(double cx) {
        this.cx.set(cx);
    }

    public final DoubleProperty cxProperty() {
        return cx;
    }

    public final double getCy() {
        return cy.get();
    }

    public final void setCy(double cy) {
        this.cy.set(cy);
    }

    public final DoubleProperty cyProperty() {
        return cy;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeDouble(getCx());
        out.writeDouble(getCx());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setCx(in.readDouble());
        setCy(in.readDouble());
    }
}
