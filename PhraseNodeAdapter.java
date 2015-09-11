package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.graph.model.PhraseNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class PhraseNodeAdapter extends LinkSupportAdapter<PhraseNode> {

    private final ObjectProperty<RelationshipType> relationshipType = new SimpleObjectProperty<>();
    private final ObjectProperty<AlternateStatus> alternateStatus = new SimpleObjectProperty<>();
    private final ObservableList<PartOfSpeechNodeAdapter> fragments = observableArrayList();

    public PhraseNodeAdapter() {
        super();
    }

    @Override
    protected void initValues(PhraseNode phraseNode) {
        super.initValues(phraseNode);
        setRelationshipType(phraseNode == null ? null : phraseNode.getRelationshipType());
        setAlternateStatus(phraseNode == null ? null : phraseNode.getAlternateStatus());
        CanvasUtil canvasUtil = CanvasUtil.getInstance();
        List<PartOfSpeechNode> fragments = phraseNode.getFragments();
        fragments.forEach(partOfSpeechNode -> getFragments().add(
                (PartOfSpeechNodeAdapter) canvasUtil.createLinkSupportAdapter(partOfSpeechNode)));
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

    public final ObservableList<PartOfSpeechNodeAdapter> getFragments() {
        return fragments;
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
}
