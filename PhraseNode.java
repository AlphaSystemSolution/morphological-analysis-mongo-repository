package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.common.model.Related;
import com.alphasystem.morphologicalanalysis.graph.model.Fragment;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PHRASE;

/**
 * @author sali
 */
public class PhraseNode extends LinkSupport {

    private static final long serialVersionUID = 449280304391482565L;

    private final ObjectProperty<Fragment> frament;

    /**
     *
     */
    public PhraseNode() {
        this(null, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
    }

    /**
     * @param fragment
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param cx
     * @param cy
     */
    public PhraseNode(Fragment fragment, Double x, Double y, Double x1,
                      Double y1, Double x2, Double y2, Double cx, Double cy) {
        super(PHRASE, null, null, x, y, x1, y1, x2, y2, 0.0, 0.0, cx, cy);
        this.frament = new SimpleObjectProperty<>();
        framentProperty().addListener((observable, oldValue, newValue) -> {
            setId(null);
            setText(null);
            if (newValue != null) {
                setId(newValue.getId());
                setText(newValue.getRelationshipType().getLabel().toUnicode());
            }
        });
        setFrament(fragment);
    }

    @Override
    public Related getRelated() {
        return getFrament();
    }

    public final Fragment getFrament() {
        return frament.get();
    }

    public final void setFrament(Fragment frament) {
        this.frament.set(frament);
    }

    public final ObjectProperty<Fragment> framentProperty() {
        return frament;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }
}
