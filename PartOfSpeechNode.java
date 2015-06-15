package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.arabic.model.ArabicLetters;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PART_OF_SPEECH;

/**
 * @author sali
 */
public class PartOfSpeechNode extends GraphNode {

    private static final long serialVersionUID = 8910009645217482464L;

    /**
     * x location for circle.
     */
    private final DoubleProperty cx;

    /**
     * y location for circle
     */
    private final DoubleProperty cy;

    private PartOfSpeech partOfSpeech;

    private Location location;

    private AbstractProperties properties;

    /**
     *
     */
    public PartOfSpeechNode() {
        this(null, null, 0d, 0d, 0d, 0d);
    }

    /**
     * @param partOfSpeech
     * @param location
     * @param x
     * @param y
     * @param cx
     * @param cy
     */
    public PartOfSpeechNode(PartOfSpeech partOfSpeech, Location location, Double x, Double y,
                            Double cx, Double cy) {
        super(PART_OF_SPEECH, null, getText(partOfSpeech, location), x, y);
        this.partOfSpeech = partOfSpeech;
        setLocation(location);
        this.cx = new SimpleDoubleProperty(cx);
        this.cy = new SimpleDoubleProperty(cy);
    }

    private static String getText(PartOfSpeech partOfSpeech, Location location) {
        if (location == null || partOfSpeech == null) {
            return null;
        }
        AbstractProperties properties = location.getProperties();
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

    private void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public AbstractProperties getProperties() {
        return properties;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (this.location != null) {
            this.properties = this.location.getProperties();
            setId(this.location.getId());
        }
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
        out.writeObject(getPartOfSpeech());
        out.writeDouble(getCx());
        out.writeDouble(getCy());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setPartOfSpeech((PartOfSpeech) in.readObject());
        setCx(in.readDouble());
        setCy(in.readDouble());
    }

    @Override
    public String toString() {
        return getText(partOfSpeech, location);
    }
}
