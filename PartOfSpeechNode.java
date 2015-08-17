package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.common.model.Related;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbMode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PART_OF_SPEECH;

/**
 * @author sali
 */
public class PartOfSpeechNode extends LinkSupport {

    private static final long serialVersionUID = 8910009645217482464L;

    private final ObjectProperty<Location> location;
    private PartOfSpeech partOfSpeech;
    private AbstractProperties properties;

    /**
     *
     */
    public PartOfSpeechNode() {
        this(null, 0d, 0d, 0d, 0d);
    }

    /**
     * @param location
     * @param x
     * @param y
     * @param cx
     * @param cy
     */
    public PartOfSpeechNode(Location location, Double x, Double y, Double cx, Double cy) {
        super(PART_OF_SPEECH, null, null, x, y, cx, cy);
        this.location = new SimpleObjectProperty<>();
        locationProperty().addListener((observable, oldLocation, newLocation) -> {
            setPartOfSpeech(null);
            setId(null);
            setText(null);
            setProperties(null);
            if (newLocation != null) {
                setPartOfSpeech(newLocation.getPartOfSpeech());
                setId(newLocation.getId());
                setText(getText(getPartOfSpeech(), getLocation()));
                setProperties(newLocation.getProperties());
            }
        });
        setLocation(location);
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
                builder.append(WORD_SPACE.toUnicode()).append(np.getStatus().getLabel().toUnicode());
                break;
            case VERB:
                VerbProperties vp = (VerbProperties) properties;
                builder.append(WORD_SPACE.toUnicode()).append(vp.getVerbType().getLabel().toUnicode());
                final VerbMode mode = vp.getMode();
                if(mode != null){
                    builder.append(WORD_SPACE.toUnicode()).append(mode.getLabel().toUnicode());
                }
                break;
        }
        return builder.toString();
    }

    @Override
    public Related getRelated() {
        return getLocation();
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

    private void setProperties(AbstractProperties properties) {
        this.properties = properties;
    }

    public final Location getLocation() {
        return location.get();
    }

    public final void setLocation(Location location) {
        this.location.set(location);
    }

    public final ObjectProperty<Location> locationProperty() {
        return location;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(getPartOfSpeech());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setPartOfSpeech((PartOfSpeech) in.readObject());
    }

    @Override
    public String toString() {
        return getText(partOfSpeech, getLocation());
    }
}
