package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author sali
 */
public class PartOfSpeechNodeAdapter extends LinkSupportAdapter<PartOfSpeechNode> {

    private final BooleanProperty hidden = new SimpleBooleanProperty();

    public PartOfSpeechNodeAdapter() {
        super();
    }

    @Override
    protected void initValues(PartOfSpeechNode graphNode) {
        super.initValues(graphNode);
        setHidden(graphNode == null ? false : graphNode.isHidden());
        hiddenProperty().addListener((observable, oldValue, newValue) -> {
            getSrc().setHidden(newValue);
        });
    }

    public boolean isHidden() {
        return hidden.get();
    }

    public void setHidden(boolean hidden) {
        this.hidden.set(hidden);
    }

    public BooleanProperty hiddenProperty() {
        return hidden;
    }

}
