package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public class TerminalNodeAdapter extends LineSupportAdapter<TerminalNode> {

    /**
     * x location for translation
     */
    private final DoubleProperty translationX = new SimpleDoubleProperty();

    /**
     * y location for translation
     */
    private final DoubleProperty translationY = new SimpleDoubleProperty();
    private final StringProperty translationText = new SimpleStringProperty();
    private final ObjectProperty<Font> translationFont = new SimpleObjectProperty<>();

    private ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = FXCollections.observableArrayList();

    public TerminalNodeAdapter() {
        super();
        translationXProperty().addListener((observable, oldValue, newValue) ->
                getSrc().setTranslationX((Double) newValue));
        translationYProperty().addListener((observable, oldValue, newValue) ->
                getSrc().setTranslationY((Double) newValue));
        translationFontProperty().addListener((observable, oldValue, newValue) ->
                getSrc().getTranslationFont().withFamily(newValue.getFamily()).withSize(newValue.getSize()));
    }

    @Override
    protected void initValues(TerminalNode terminalNode) {
        super.initValues(terminalNode);
        setTranslationX(terminalNode == null ? null : terminalNode.getTranslationX());
        setTranslationY(terminalNode == null ? null : terminalNode.getTranslationY());
        setTranslationText(terminalNode == null ? null : terminalNode.getToken().getTranslation());
        partOfSpeeches.remove(0, partOfSpeeches.size());
        if (terminalNode != null) {
            setTranslationFont(CanvasUtil.getInstance().createFont(terminalNode.getTranslationFont()));
            terminalNode.getPartOfSpeechNodes().forEach(partOfSpeechNode -> {
                PartOfSpeechNodeAdapter partOfSpeechNodeAdapter = new PartOfSpeechNodeAdapter();
                partOfSpeechNodeAdapter.setParent(this);
                partOfSpeechNodeAdapter.setSrc(partOfSpeechNode);
                partOfSpeeches.add(partOfSpeechNodeAdapter);
            });
        }
    }

    public final double getTranslationX() {
        return translationX.get();
    }

    public final void setTranslationX(double translationX) {
        this.translationX.set(translationX);
    }

    public final DoubleProperty translationXProperty() {
        return translationX;
    }

    public final double getTranslationY() {
        return translationY.get();
    }

    public final void setTranslationY(double translationY) {
        this.translationY.set(translationY);
    }

    public final DoubleProperty translationYProperty() {
        return translationY;
    }

    public final ObservableList<PartOfSpeechNodeAdapter> getPartOfSpeeches() {
        return partOfSpeeches;
    }

    public final void setPartOfSpeeches(ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches) {
        this.partOfSpeeches = partOfSpeeches;
    }

    public final String getTranslationText() {
        return translationText.get();
    }

    public final void setTranslationText(String translationText) {
        this.translationText.set(translationText);
    }

    public final StringProperty translationTextProperty() {
        return translationText;
    }

    public final Font getTranslationFont() {
        return translationFont.get();
    }

    public final void setTranslationFont(Font translationFont) {
        this.translationFont.set(translationFont);
    }

    public final ObjectProperty<Font> translationFontProperty() {
        return translationFont;
    }
}
