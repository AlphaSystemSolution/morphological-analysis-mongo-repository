package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.arabic.model.ArabicLetters;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PART_OF_SPEECH;

/**
 * @author sali
 */
public class PartOfSpeechNode extends GraphNode {

    /**
     * x location for circle.
     */
    private final DoubleProperty cx;

    /**
     * y location for circle
     */
    private final DoubleProperty cy;

    private final BooleanProperty excluded;

    private final PartOfSpeech partOfSpeech;

    private final Location location;

    private final AbstractProperties properties;

    /**
     *
     */
    public PartOfSpeechNode() {
        this(null, null, null, 0d, 0d, 0d, 0d, false);
    }

    /**
     * @param partOfSpeech
     * @param location
     * @param id
     * @param x
     * @param y
     * @param cx
     * @param cy
     * @param excluded
     */
    public PartOfSpeechNode(PartOfSpeech partOfSpeech, Location location, String id, Double x, Double y,
                            Double cx, Double cy, Boolean excluded) {
        super(PART_OF_SPEECH, id, getText(partOfSpeech, location.getProperties()), x, y);
        this.partOfSpeech = partOfSpeech;
        this.location = location;
        this.properties = location.getProperties();
        this.excluded = new SimpleBooleanProperty(excluded);
        this.cx = new SimpleDoubleProperty(cx);
        this.cy = new SimpleDoubleProperty(cy);
    }

    private static String getText(PartOfSpeech partOfSpeech, AbstractProperties properties) {
        StringBuilder builder = new StringBuilder(partOfSpeech.getLabel().toUnicode());
        switch (partOfSpeech) {
            case NOUN:
                NounProperties np = (NounProperties) properties;
                builder.append(ArabicLetters.WORD_SPACE.toUnicode()).
                        append(np.getStatus().getLabel().toUnicode());
                break;
        }
        return builder.toString();
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public AbstractProperties getProperties() {
        return properties;
    }

    public Location getLocation() {
        return location;
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

    public final boolean isExcluded() {
        return excluded.get();
    }

    public final void setExcluded(boolean excluded) {
        this.excluded.set(excluded);
    }

    public final BooleanProperty excludedProperty() {
        return excluded;
    }

    @Override
    public String toString() {
        return getText(partOfSpeech, properties);
    }
}
