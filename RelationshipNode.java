package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.common.model.Related;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.RELATIONSHIP;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CubicCurveHelper.calculateCurvePoint;
import static com.alphasystem.util.IdGenerator.nextId;
import static java.lang.String.format;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.web;

/**
 * @author sali
 */
public class RelationshipNode extends GraphNode {

    private static final long serialVersionUID = 1815348680369408820L;

    private static final char LEFT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK = '\u00AB';

    private static final char RIGHT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK = '\u00BB';

    private final ReadOnlyDoubleWrapper startX;

    private final ReadOnlyDoubleWrapper startY;

    private final DoubleProperty controlX1;

    private final DoubleProperty controlY1;

    private final DoubleProperty controlX2;

    private final DoubleProperty controlY2;

    private final ReadOnlyDoubleWrapper endX;

    private final ReadOnlyDoubleWrapper endY;

    private final DoubleProperty t1;

    private final DoubleProperty t2;

    private final DoubleProperty arrowPointX1;

    private final DoubleProperty arrowPointY1;

    private final DoubleProperty arrowPointX2;

    private final DoubleProperty arrowPointY2;

    private final DoubleProperty curvePointX;

    private final DoubleProperty curvePointY;

    private final ObjectProperty<Relationship> relationship;

    private final ObjectProperty<LinkSupport> dependentNode;

    private final ObjectProperty<LinkSupport> ownerNode;

    private final ReadOnlyObjectWrapper<Color> stroke;

    private final LocationRepository locationRepository = RepositoryTool.getInstance()
            .getRepositoryUtil().getLocationRepository();

    /**
     *
     */
    public RelationshipNode() {
        this(null, null, null, 0d, 0d, 0d, 0d, 0d, 0d, 0.5, 0.55);
    }

    /**
     * @param relationship
     * @param dependentNode
     * @param ownerNode
     * @param x
     * @param y
     * @param controlX1
     * @param controlY1
     * @param controlX2
     * @param controlY2
     */
    public RelationshipNode(Relationship relationship, LinkSupport dependentNode, LinkSupport ownerNode,
                            Double x, Double y, Double controlX1, Double controlY1, Double controlX2,
                            Double controlY2, Double t1, Double t2) {
        super(RELATIONSHIP, null, null, x, y);
        this.relationship = new SimpleObjectProperty<>();
        this.dependentNode = new SimpleObjectProperty<>();
        this.ownerNode = new SimpleObjectProperty<>();
        this.stroke = new ReadOnlyObjectWrapper<>();
        this.startX = new ReadOnlyDoubleWrapper();
        this.startY = new ReadOnlyDoubleWrapper();
        this.controlX1 = new SimpleDoubleProperty();
        this.controlY1 = new SimpleDoubleProperty();
        this.controlX2 = new SimpleDoubleProperty();
        this.controlY2 = new SimpleDoubleProperty();
        this.endX = new ReadOnlyDoubleWrapper();
        this.endY = new ReadOnlyDoubleWrapper();
        this.t1 = new SimpleDoubleProperty();
        this.t2 = new SimpleDoubleProperty();
        this.arrowPointX1 = new SimpleDoubleProperty();
        this.arrowPointY1 = new SimpleDoubleProperty();
        this.arrowPointX2 = new SimpleDoubleProperty();
        this.arrowPointY2 = new SimpleDoubleProperty();
        this.curvePointX = new SimpleDoubleProperty();
        this.curvePointY = new SimpleDoubleProperty();
        initListeners();
        setRelationship(relationship);
        setDependentNode(dependentNode);
        setOwnerNode(ownerNode);
        setControlX1(controlX1);
        setControlY1(controlY1);
        setControlX2(controlX2);
        setControlY2(controlY2);
        setT1(t1);
        setT2(t2);
    }

    private void initListeners() {
        relationshipProperty().addListener((observable, oldValue, newValue) -> {
            setStroke(BLACK);
            setId(nextId());
            setText((String) null);
            if (newValue != null) {
                RelationshipType relationship = newValue.getRelationship();
                setStroke(web(relationship.getColorCode()));
                setId(newValue.getId());
                setText(newValue);
            }
        });
        dependentNodeProperty().addListener((observable, oldValue, newValue) -> {
            setStartX(0d);
            setStartY(0d);
            if (newValue != null) {
                setStartX(newValue.getCx() + newValue.getTranslateX());
                setStartY(newValue.getCy() + newValue.getTranslateY());
            }
            updateArrow();
        });
        ownerNodeProperty().addListener((observable, oldValue, newValue) -> {
            setEndX(0d);
            setEndY(0d);
            if (newValue != null) {
                setEndX(newValue.getCx() + newValue.getTranslateX());
                setEndY(newValue.getCy() + newValue.getTranslateY());
                updateArrow();
            }
        });
        controlX1Property().addListener((observable, oldValue, newValue) -> updateArrow(oldValue, newValue));
        controlY1Property().addListener((observable, oldValue, newValue) -> updateArrow(oldValue, newValue));
        controlX2Property().addListener((observable, oldValue, newValue) -> updateArrow(oldValue, newValue));
        controlY2Property().addListener((observable, oldValue, newValue) -> updateArrow(oldValue, newValue));
        t1Property().addListener((observable, oldValue, newValue) -> updateArrow(oldValue, newValue));
        t2Property().addListener((observable, oldValue, newValue) -> updateArrow(oldValue, newValue));
    }

    private void setText(Relationship relationship) {
        RelationshipType type = relationship.getRelationship();
        String text = type.getLabel().toUnicode();
        Related owner = relationship.getOwner();

        Location ol = locationRepository.findByDisplayName(owner.getDisplayName());
        if (ol != null) {
            PartOfSpeech pos = ol.getPartOfSpeech();
            switch (pos) {
                case ACCUSSATIVE_PARTICLE:
                    text = format("%s %s %s %s", type.getLabel().toUnicode(),
                            LEFT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK, ol.getLocationWord().toUnicode(),
                            RIGHT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK);
                    break;
            }
        }

        setText(text);
    }

    private void updateArrow(Number oldValue, Number newValue) {
        if (!Objects.equals(newValue, oldValue)) {
            updateArrow();
        }
    }

    private void updateArrow() {
        Point2D point = calculateCurvePoint(getT1(), getStartX(), getStartY(), getControlX1(), getControlY1(),
                getControlX2(), getControlY2(), getEndX(), getEndY());
        arrowPointX1.setValue(point.getX());
        arrowPointY1.setValue(point.getY() + 5);

        arrowPointX2.setValue(point.getX());
        arrowPointY2.setValue(point.getY() - 5);

        point = calculateCurvePoint(getT2(), getStartX(), getStartY(), getControlX1(), getControlY1(),
                getControlX2(), getControlY2(), getEndX(), getEndY());
        curvePointX.setValue(point.getX());
        curvePointY.setValue(point.getY());
    }

    public Color getStroke() {
        return stroke.get();
    }

    private void setStroke(Color stroke) {
        this.stroke.set(stroke);
    }

    public ReadOnlyObjectProperty<Color> strokeProperty() {
        return stroke.getReadOnlyProperty();
    }

    public final Relationship getRelationship() {
        return relationship.get();
    }

    public final void setRelationship(Relationship relationship) {
        this.relationship.set(relationship);
    }

    public final ObjectProperty<Relationship> relationshipProperty() {
        return relationship;
    }

    public final LinkSupport getOwnerNode() {
        return ownerNode.get();
    }

    public final void setOwnerNode(LinkSupport ownerNode) {
        this.ownerNode.set(ownerNode);
        LinkSupport node = getOwnerNode();
        if (node != null) {
            node.cxProperty().addListener((observable, oldValue, newValue) -> {
                setEndX(((Double) newValue) + node.getTranslateX());
            });
            node.cyProperty().addListener((observable, oldValue, newValue) -> {
                setEndY(((Double) newValue) + node.getTranslateY());
            });
            node.translateXProperty().addListener((observable, oldValue, newValue) -> {
                setEndX(node.getCx() + ((Double) newValue));
            });
            node.translateYProperty().addListener((observable, oldValue, newValue) -> {
                setEndY(node.getCy() + ((Double) newValue));
            });
        }
    }

    public final ObjectProperty<LinkSupport> ownerNodeProperty() {
        return ownerNode;
    }

    public final LinkSupport getDependentNode() {
        return dependentNode.get();
    }

    public final void setDependentNode(LinkSupport dependentNode) {
        this.dependentNode.set(dependentNode);
        LinkSupport node = getDependentNode();
        if (node != null) {
            node.cxProperty().addListener((observable, oldValue, newValue) -> {
                setStartX(((Double) newValue) + node.getTranslateX());
            });
            node.cyProperty().addListener((observable, oldValue, newValue) -> {
                setStartY(((Double) newValue) + node.getTranslateY());
            });
            node.translateXProperty().addListener((observable, oldValue, newValue) -> {
                setStartX(node.getCx() + ((Double) newValue));
            });
            node.translateYProperty().addListener((observable, oldValue, newValue) -> {
                setStartY(node.getCy() + ((Double) newValue));
            });
        }
    }

    public final ObjectProperty<LinkSupport> dependentNodeProperty() {
        return dependentNode;
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

    public final double getStartX() {
        return startX.get();
    }

    private void setStartX(double startX) {
        this.startX.set(startX);
    }

    public final ReadOnlyDoubleProperty startXProperty() {
        return startX.getReadOnlyProperty();
    }

    public final double getStartY() {
        return startY.get();
    }

    private void setStartY(double startY) {
        this.startY.set(startY);
    }

    public final ReadOnlyDoubleProperty startYProperty() {
        return startY.getReadOnlyProperty();
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
        out.writeObject(getDependentNode());
        out.writeObject(getOwnerNode());
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
        setDependentNode((LinkSupport) in.readObject());
        setOwnerNode((LinkSupport) in.readObject());
        updateArrow();
    }
}
