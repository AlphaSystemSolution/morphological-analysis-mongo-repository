package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PHRASE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType.NONE;

/**
 * @author sali
 */
public class PhraseNode extends LineSupport {

    private static final long serialVersionUID = 449280304391482565L;

    /**
     * x location for circle.
     */
    private final DoubleProperty cx;

    /**
     * y location for circle
     */
    private final DoubleProperty cy;

    private final ObjectProperty<RelationshipType> grammaticalRelationship;

    /**
     *
     */
    public PhraseNode() {
        this(NONE, null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
    }

    /**
     * @param relationshipType
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param cx
     * @param cy
     */
    public PhraseNode(RelationshipType relationshipType, String id, Double x, Double y, Double x1,
                      Double y1, Double x2, Double y2, Double cx, Double cy) {
        super(PHRASE, id, relationshipType.getLabel().toUnicode(), x, y, x1, y1, x2, y2, 0.0, 0.0);
        this.grammaticalRelationship = new SimpleObjectProperty<>(relationshipType);
        this.cx = new SimpleDoubleProperty(cx);
        this.cy = new SimpleDoubleProperty(cy);
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

    public double getCx() {
        return cx.get();
    }

    public void setCx(double cx) {
        this.cx.set(cx);
    }

    public final DoubleProperty cxProperty() {
        return cx;
    }

    public double getCy() {
        return cy.get();
    }

    public void setCy(double cy) {
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
        out.writeObject(getGrammaticalRelationship());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setCx(in.readDouble());
        setCy(in.readDouble());
        setGrammaticalRelationship((RelationshipType) in.readObject());
    }
}
