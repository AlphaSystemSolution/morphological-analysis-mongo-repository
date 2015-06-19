package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.common.model.Related;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PHRASE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType.NONE;

/**
 * @author sali
 */
public class PhraseNode extends LinkSupport {

    private static final long serialVersionUID = 449280304391482565L;


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
        super(PHRASE, id, relationshipType.getLabel().toUnicode(), x, y, x1, y1, x2, y2, 0.0, 0.0, cx, cy);
        this.grammaticalRelationship = new SimpleObjectProperty<>(relationshipType);
    }

    @Override
    public Related getRelated() {
        return null;
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(getGrammaticalRelationship());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setGrammaticalRelationship((RelationshipType) in.readObject());
    }
}
