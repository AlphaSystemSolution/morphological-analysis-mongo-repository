package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CubicCurveHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.RELATIONSHIP;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType.NONE;
import static javafx.scene.paint.Color.web;

/**
 * @author sali
 */
public class RelationshipNode extends GraphNode {

    private static final long serialVersionUID = 1815348680369408820L;

    private final DoubleProperty startX;

    private final DoubleProperty startY;

    private final DoubleProperty controlX1;

    private final DoubleProperty controlY1;

    private final DoubleProperty controlX2;

    private final DoubleProperty controlY2;

    private final DoubleProperty endX;

    private final DoubleProperty endY;

    private final DoubleProperty t1;

    private final DoubleProperty t2;

    private final DoubleProperty arrowPointX1;

    private final DoubleProperty arrowPointY1;

    private final DoubleProperty arrowPointX2;

    private final DoubleProperty arrowPointY2;

    private final DoubleProperty curvePointX;

    private final DoubleProperty curvePointY;

    private final ObjectProperty<RelationshipType> grammaticalRelationship;

    private final ObjectProperty<Color> stroke;

    /**
     *
     */
    public RelationshipNode() {
        this(NONE, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0.5, 0.55);
    }

    /**
     * @param relationshipType
     * @param id
     * @param x
     * @param y
     * @param startX
     * @param startY
     * @param controlX1
     * @param controlY1
     * @param controlX2
     * @param controlY2
     * @param endX
     * @param endY
     */
    public RelationshipNode(RelationshipType relationshipType, String id, Double x, Double y,
                            Double startX, Double startY, Double controlX1, Double controlY1, Double controlX2,
                            Double controlY2, Double endX, Double endY, Double t1, Double t2) {
        super(RELATIONSHIP, id, relationshipType.getLabel().toUnicode(), x, y);
        this.grammaticalRelationship = new SimpleObjectProperty<>(relationshipType);
        this.stroke = new SimpleObjectProperty<>(web(relationshipType.getColorCode()));
        this.startX = new SimpleDoubleProperty(startX);
        this.startY = new SimpleDoubleProperty(startY);
        this.controlX1 = new SimpleDoubleProperty(controlX1);
        this.controlY1 = new SimpleDoubleProperty(controlY1);
        this.controlX2 = new SimpleDoubleProperty(controlX2);
        this.controlY2 = new SimpleDoubleProperty(controlY2);
        this.endX = new SimpleDoubleProperty(endX);
        this.endY = new SimpleDoubleProperty(endY);
        this.t1 = new SimpleDoubleProperty(t1);
        this.t2 = new SimpleDoubleProperty(t2);
        this.arrowPointX1 = new SimpleDoubleProperty();
        this.arrowPointY1 = new SimpleDoubleProperty();
        this.arrowPointX2 = new SimpleDoubleProperty();
        this.arrowPointY2 = new SimpleDoubleProperty();
        this.curvePointX = new SimpleDoubleProperty();
        this.curvePointY = new SimpleDoubleProperty();
        updateArrow();
        initListeners();
    }

    private void initListeners() {
        grammaticalRelationshipProperty().addListener((observable, oldValue, newValue) -> {
            this.stroke.setValue(web(newValue.getColorCode()));
        });
        startXProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        startYProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        controlX1Property().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        controlY1Property().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        controlX2Property().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        controlY2Property().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        endXProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        endYProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        t1Property().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
        t2Property().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                updateArrow();
            }
        });
    }

    private void updateArrow() {
        Point2D point = CubicCurveHelper.calculateCurvePoint(getT1(), getStartX(), getStartY(), getControlX1(), getControlY1(),
                getControlX2(), getControlY2(), getEndX(), getEndY());
        arrowPointX1.setValue(point.getX());
        arrowPointY1.setValue(point.getY() + 5);

        arrowPointX2.setValue(point.getX());
        arrowPointY2.setValue(point.getY() - 5);

        point = CubicCurveHelper.calculateCurvePoint(getT2(), getStartX(), getStartY(), getControlX1(), getControlY1(),
                getControlX2(), getControlY2(), getEndX(), getEndY());
        curvePointX.setValue(point.getX());
        curvePointY.setValue(point.getY());
    }

    public Color getStroke() {
        return stroke.get();
    }

    public void setStroke(Color stroke) {
        this.stroke.set(stroke);
    }

    public ObjectProperty<Color> strokeProperty() {
        return stroke;
    }

    public final RelationshipType getGrammaticalRelationship() {
        return grammaticalRelationship.get();
    }

    public final void setGrammaticalRelationship(RelationshipType relationshipType) {
        this.grammaticalRelationship.set(relationshipType);
    }

    public final ObjectProperty<RelationshipType> grammaticalRelationshipProperty() {
        return grammaticalRelationship;
    }

    public double getControlX1() {
        return controlX1.get();
    }

    public void setControlX1(double controlX1) {
        this.controlX1.set(controlX1);
    }

    public final DoubleProperty controlX1Property() {
        return controlX1;
    }

    public double getControlX2() {
        return controlX2.get();
    }

    public void setControlX2(double controlX2) {
        this.controlX2.set(controlX2);
    }

    public final DoubleProperty controlX2Property() {
        return controlX2;
    }

    public double getControlY1() {
        return controlY1.get();
    }

    public void setControlY1(double controlY1) {
        this.controlY1.set(controlY1);
    }

    public final DoubleProperty controlY1Property() {
        return controlY1;
    }

    public double getControlY2() {
        return controlY2.get();
    }

    public void setControlY2(double controlY2) {
        this.controlY2.set(controlY2);
    }

    public final DoubleProperty controlY2Property() {
        return controlY2;
    }

    public double getEndX() {
        return endX.get();
    }

    public void setEndX(double endX) {
        this.endX.set(endX);
    }

    public final DoubleProperty endXProperty() {
        return endX;
    }

    public double getEndY() {
        return endY.get();
    }

    public void setEndY(double endY) {
        this.endY.set(endY);
    }

    public final DoubleProperty endYProperty() {
        return endY;
    }

    public double getStartX() {
        return startX.get();
    }

    public void setStartX(double startX) {
        this.startX.set(startX);
    }

    public final DoubleProperty startXProperty() {
        return startX;
    }

    public double getStartY() {
        return startY.get();
    }

    public void setStartY(double startY) {
        this.startY.set(startY);
    }

    public final DoubleProperty startYProperty() {
        return startY;
    }

    public final double getT1() {
        return t1.get();
    }

    public final void setT1(double t1) {
        this.t1.set(t1);
    }

    public final DoubleProperty t1Property() {
        return t1;
    }

    public final double getT2() {
        return t2.get();
    }

    public final void setT2(double t2) {
        this.t2.set(t2);
    }

    public final DoubleProperty t2Property() {
        return t2;
    }

    public double getArrowPointX1() {
        return arrowPointX1.get();
    }

    public double getArrowPointY1() {
        return arrowPointY1.get();
    }

    public double getArrowPointX2() {
        return arrowPointX2.get();
    }

    public double getArrowPointY2() {
        return arrowPointY2.get();
    }

    public double getCurvePointX() {
        return curvePointX.get();
    }

    public double getCurvePointY() {
        return curvePointY.get();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeDouble(getStartX());
        out.writeDouble(getStartY());
        out.writeDouble(getControlX1());
        out.writeDouble(getControlY1());
        out.writeDouble(getControlX2());
        out.writeDouble(getControlY1());
        out.writeDouble(getEndX());
        out.writeDouble(getEndY());
        out.writeDouble(getT1());
        out.writeDouble(getT2());
        out.writeObject(getGrammaticalRelationship());
        out.writeObject(getStroke());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setStartX(in.readDouble());
        setStartY(in.readDouble());
        setControlX1(in.readDouble());
        setControlY1(in.readDouble());
        setControlX2(in.readDouble());
        setControlY2(in.readDouble());
        setEndX(in.readDouble());
        setEndY(in.readDouble());
        setT1(in.readDouble());
        setT2(in.readDouble());
        setGrammaticalRelationship((RelationshipType) in.readObject());
        setStroke((Color) in.readObject());
        updateArrow();
    }
}
