package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import static javafx.scene.paint.Color.web;

/**
 * @author sali
 */
public class RelationshipNodeAdapter extends GraphNodeAdapter<RelationshipNode> {

    private final DoubleProperty controlX1 = new SimpleDoubleProperty();
    private final DoubleProperty controlY1 = new SimpleDoubleProperty();
    private final DoubleProperty controlX2 = new SimpleDoubleProperty();
    private final DoubleProperty controlY2 = new SimpleDoubleProperty();
    private final DoubleProperty t1 = new SimpleDoubleProperty();
    private final DoubleProperty t2 = new SimpleDoubleProperty();
    private final ReadOnlyObjectWrapper<Color> stroke = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<RelationshipType> relationshipType = new SimpleObjectProperty<>();
    private final ObjectProperty<AlternateStatus> alternateStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<LinkSupportAdapter> dependent = new SimpleObjectProperty<>();
    private final ObjectProperty<LinkSupportAdapter> owner = new SimpleObjectProperty<>();

    public RelationshipNodeAdapter() {
        super();
        controlX1Property().addListener((observable, oldValue, newValue) -> getSrc().setControlX1((Double) newValue));
        controlX2Property().addListener((observable, oldValue, newValue) -> getSrc().setControlX2((Double) newValue));
        controlY1Property().addListener((observable, oldValue, newValue) -> getSrc().setControlY1((Double) newValue));
        controlY2Property().addListener((observable, oldValue, newValue) -> getSrc().setControlY2((Double) newValue));
        t1Property().addListener((observable, oldValue, newValue) -> getSrc().setT1((Double) newValue));
        t2Property().addListener((observable, oldValue, newValue) -> getSrc().setT2((Double) newValue));
        relationshipTypeProperty().addListener((observable, oldValue, newValue) -> {
            stroke.setValue(newValue == null ? Color.BLACK : web(newValue.getColorCode()));
        });
    }

    @Override
    protected void initValues(RelationshipNode relationshipNode) {
        super.initValues(relationshipNode);
        setControlX1(relationshipNode == null ? 0.0 : relationshipNode.getControlX1());
        setControlX2(relationshipNode == null ? 0.0 : relationshipNode.getControlX2());
        setControlY1(relationshipNode == null ? 0.0 : relationshipNode.getControlY1());
        setControlY2(relationshipNode == null ? 0.0 : relationshipNode.getControlY2());
        setT1(relationshipNode == null ? 0.0 : relationshipNode.getT1());
        setT2(relationshipNode == null ? 0.0 : relationshipNode.getT2());
        setRelationshipType(relationshipNode == null ? null : relationshipNode.getRelationshipType());
        setAlternateStatus(relationshipNode == null ? null : relationshipNode.getAlternateStatus());
        CanvasUtil canvasUtil = CanvasUtil.getInstance();
        setDependent(relationshipNode == null ? null :
                canvasUtil.createLinkSupportAdapter(relationshipNode.getDependent()));
        setOwner(relationshipNode == null ? null : canvasUtil.createLinkSupportAdapter(relationshipNode.getOwner()));
    }

    public final RelationshipType getRelationshipType() {
        return relationshipType.get();
    }

    public final void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType.set(relationshipType);
    }

    public final ObjectProperty<RelationshipType> relationshipTypeProperty() {
        return relationshipType;
    }

    public final AlternateStatus getAlternateStatus() {
        return alternateStatus.get();
    }

    public final void setAlternateStatus(AlternateStatus alternateStatus) {
        this.alternateStatus.set(alternateStatus);
    }

    public final ObjectProperty<AlternateStatus> alternateStatusProperty() {
        return alternateStatus;
    }

    public final double getControlX1() {
        return controlX1.get();
    }

    public final void setControlX1(double controlX1) {
        this.controlX1.set(controlX1);
    }

    public final DoubleProperty controlX1Property() {
        return controlX1;
    }

    public final double getControlX2() {
        return controlX2.get();
    }

    public final void setControlX2(double controlX2) {
        this.controlX2.set(controlX2);
    }

    public final DoubleProperty controlX2Property() {
        return controlX2;
    }

    public final double getControlY1() {
        return controlY1.get();
    }

    public final void setControlY1(double controlY1) {
        this.controlY1.set(controlY1);
    }

    public final DoubleProperty controlY1Property() {
        return controlY1;
    }

    public final double getControlY2() {
        return controlY2.get();
    }

    public final void setControlY2(double controlY2) {
        this.controlY2.set(controlY2);
    }

    public final DoubleProperty controlY2Property() {
        return controlY2;
    }

    public final Color getStroke() {
        return stroke.get();
    }

    public final ReadOnlyObjectProperty<Color> strokeProperty() {
        return stroke.getReadOnlyProperty();
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

    public final LinkSupportAdapter getDependent() {
        return dependent.get();
    }

    public final void setDependent(LinkSupportAdapter dependent) {
        this.dependent.set(dependent);
    }

    public final ObjectProperty<LinkSupportAdapter> dependentProperty() {
        return dependent;
    }

    public final LinkSupportAdapter getOwner() {
        return owner.get();
    }

    public final void setOwner(LinkSupportAdapter owner) {
        this.owner.set(owner);
    }

    public final ObjectProperty<LinkSupportAdapter> ownerProperty() {
        return owner;
    }
}
