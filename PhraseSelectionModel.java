package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.NONE_SELECTED;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType.NONE;

/**
 * @author sali
 */
public final class PhraseSelectionModel {

    private final ReadOnlyStringWrapper firstNodeLabel;
    private final ReadOnlyStringWrapper lastNodeLabel;
    private final ObjectProperty<RelationshipType> relationship = new SimpleObjectProperty<>(NONE);
    private ObjectProperty<TerminalNode> firstNode = new SimpleObjectProperty<>();
    private ObjectProperty<TerminalNode> lastNode = new SimpleObjectProperty<>();

    public PhraseSelectionModel() {
        firstNodeLabel = new ReadOnlyStringWrapper(getLabel(getFirstNode()));
        lastNodeLabel = new ReadOnlyStringWrapper(getLabel(getLastNode()));
        firstNodeProperty().addListener((observable, oldValue, newValue) -> {
            firstNodeLabel.setValue(getLabel(getFirstNode()));
        });
        lastNodeProperty().addListener((observable, oldValue, newValue) -> {
            lastNodeLabel.setValue(getLabel(getLastNode()));
        });
    }

    private String getLabel(TerminalNode node) {
        return node == null ? NONE_SELECTED : node.getText();
    }

    public final TerminalNode getFirstNode() {
        return firstNode.get();
    }

    public void setFirstNode(TerminalNode firstNode) {
        this.firstNode.set(firstNode);
    }

    public String getFirstNodeLabel() {
        return firstNodeLabelProperty().get();
    }

    public ReadOnlyStringProperty firstNodeLabelProperty() {
        return firstNodeLabel.getReadOnlyProperty();
    }

    public final ObjectProperty<TerminalNode> firstNodeProperty() {
        return firstNode;
    }

    public final TerminalNode getLastNode() {
        return lastNode.get();
    }

    public final void setLastNode(TerminalNode lastNode) {
        this.lastNode.set(lastNode);
    }

    public String getLastNodeLabel() {
        return lastNodeLabelProperty().get();
    }

    public ReadOnlyStringProperty lastNodeLabelProperty() {
        return lastNodeLabel.getReadOnlyProperty();
    }

    public final ObjectProperty<TerminalNode> lastNodeProperty() {
        return lastNode;
    }

    public final RelationshipType getRelationship() {
        return relationship.get();
    }

    public final void setRelationship(RelationshipType relationship) {
        this.relationship.set(relationship);
    }

    public final ObjectProperty<RelationshipType> relationshipProperty() {
        return relationship;
    }

    public BooleanBinding uninitialized() {
        return uninitializedRelationship().and(uninitializedFirstNode().and(uninitializedLastNode()));
    }

    public BooleanBinding uninitializedRelationship() {
        return relationshipProperty().isNull().or(relationshipProperty().isEqualTo(NONE));
    }

    public BooleanBinding uninitializedFirstNode() {
        return firstNodeProperty().isNull();
    }

    public BooleanBinding uninitializedLastNode() {
        return lastNodeProperty().isNull();
    }

    public void reset() {
        setRelationship(NONE);
        setFirstNode(null);
        setLastNode(null);
    }
}
