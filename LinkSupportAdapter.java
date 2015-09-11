package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author sali
 */
public class LinkSupportAdapter<N extends LinkSupport> extends LineSupportAdapter<N> {

    /**
     * x location for circle.
     */
    private final DoubleProperty cx = new SimpleDoubleProperty();

    /**
     * y location for circle
     */
    private final DoubleProperty cy = new SimpleDoubleProperty();

    protected LinkSupportAdapter() {
        super();
        cxProperty().addListener((observable, oldValue, newValue) -> getSrc().setCx((Double) newValue));
        cyProperty().addListener((observable, oldValue, newValue) -> getSrc().setCy((Double) newValue));
    }

    @Override
    protected void initValues(N graphNode) {
        super.initValues(graphNode);
        setCx(graphNode == null ? null : graphNode.getCx());
        setCy(graphNode == null ? null : graphNode.getCy());
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
}
