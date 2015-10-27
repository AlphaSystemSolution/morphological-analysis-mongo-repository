package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import javafx.beans.property.*;

/**
 * @author sali
 */
public class GraphMetaInfoAdapter {

    private static final TerminalNode DUMMY_TERMINAL_NODE = new TerminalNode();
    private static final PartOfSpeechNode DUMMY_POS_NODE = new PartOfSpeechNode();

    private final ObjectProperty<GraphMetaInfo> graphMetaInfo = new SimpleObjectProperty<>();
    private final DoubleProperty width = new SimpleDoubleProperty();
    private final DoubleProperty height = new SimpleDoubleProperty();
    private final DoubleProperty tokenWidth = new SimpleDoubleProperty();
    private final DoubleProperty tokenHeight = new SimpleDoubleProperty();
    private final DoubleProperty gapBetweenTokens = new SimpleDoubleProperty();
    private final BooleanProperty showGridLines = new SimpleBooleanProperty();
    private final BooleanProperty showOutLines = new SimpleBooleanProperty();
    private final BooleanProperty debugMode = new SimpleBooleanProperty();
    private final ObjectProperty<FontMetaInfo> terminalFont = new SimpleObjectProperty<>();
    private final ObjectProperty<FontMetaInfo> posFont = new SimpleObjectProperty<>();
    private final ObjectProperty<FontMetaInfo> translationFont = new SimpleObjectProperty<>();

    public GraphMetaInfoAdapter(GraphMetaInfo src) {
        graphMetaInfoProperty().addListener((observable, oldValue, newValue) -> {
            setWidth(newValue == null ? null : newValue.getWidth());
            setHeight(newValue == null ? null : newValue.getHeight());
            setGapBetweenTokens(newValue == null ? null : newValue.getGapBetweenTokens());
            setTokenWidth(newValue == null ? null : newValue.getTokenWidth());
            setTokenHeight(newValue == null ? null : newValue.getTokenHeight());
            setShowGridLines(newValue == null ? true : newValue.isShowGridLines());
            setShowOutLines(newValue == null ? true : newValue.isShowOutLines());
            setDebugMode(newValue == null ? true : newValue.isDebugMode());
            setTerminalFont(DUMMY_TERMINAL_NODE.getFont());
            setTranslationFont(DUMMY_TERMINAL_NODE.getTranslationFont());
            setPosFont(DUMMY_POS_NODE.getFont());
        });
        setGraphMetaInfo(src);
        widthProperty().addListener((observable, oldValue, newValue) ->
                graphMetaInfoProperty().get().setWidth((Double) newValue));
        heightProperty().addListener((observable, oldValue, newValue) ->
                graphMetaInfoProperty().get().setHeight((Double) newValue));
        tokenWidthProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfoProperty().get().setTokenWidth((Double) newValue);
        });
        tokenHeightProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfoProperty().get().setTokenHeight((Double) newValue);
        });
        gapBetweenTokensProperty().addListener((observable, oldValue, newValue) ->
                graphMetaInfoProperty().get().setGapBetweenTokens((Double) newValue));
        showGridLinesProperty().addListener((observable, oldValue, newValue) ->
                graphMetaInfoProperty().get().setShowGridLines(newValue));
        showOutLinesProperty().addListener((observable, oldValue, newValue) ->
                graphMetaInfoProperty().get().setShowOutLines(newValue));
        debugModeProperty().addListener((observable, oldValue, newValue) ->
                graphMetaInfoProperty().get().setDebugMode(newValue));
    }

    public FontMetaInfo getTranslationFont() {
        return translationFont.get();
    }

    public void setTranslationFont(FontMetaInfo translationFont) {
        this.translationFont.set(translationFont);
    }

    public ObjectProperty<FontMetaInfo> translationFontProperty() {
        return translationFont;
    }

    public FontMetaInfo getTerminalFont() {
        return terminalFont.get();
    }

    public void setTerminalFont(FontMetaInfo terminalFont) {
        this.terminalFont.set(terminalFont);
    }

    public ObjectProperty<FontMetaInfo> terminalFontProperty() {
        return terminalFont;
    }

    public FontMetaInfo getPosFont() {
        return posFont.get();
    }

    public void setPosFont(FontMetaInfo posFont) {
        this.posFont.set(posFont);
    }

    public ObjectProperty<FontMetaInfo> posFontProperty() {
        return posFont;
    }

    public final GraphMetaInfo getGraphMetaInfo() {
        return graphMetaInfo.get();
    }

    public final void setGraphMetaInfo(GraphMetaInfo graphMetaInfo) {
        this.graphMetaInfo.set(graphMetaInfo);
    }

    public final ObjectProperty<GraphMetaInfo> graphMetaInfoProperty() {
        return graphMetaInfo;
    }

    public final boolean isDebugMode() {
        return debugMode.get();
    }

    public final void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public final BooleanProperty debugModeProperty() {
        return debugMode;
    }

    public final double getGapBetweenTokens() {
        return gapBetweenTokens.get();
    }

    public final void setGapBetweenTokens(double gapBetweenTokens) {
        this.gapBetweenTokens.set(gapBetweenTokens);
    }

    public final DoubleProperty gapBetweenTokensProperty() {
        return gapBetweenTokens;
    }

    public final double getHeight() {
        return height.get();
    }

    public final void setHeight(double height) {
        this.height.set(height);
    }

    public final DoubleProperty heightProperty() {
        return height;
    }

    public final boolean isShowGridLines() {
        return showGridLines.get();
    }

    public final void setShowGridLines(boolean showGridLines) {
        this.showGridLines.set(showGridLines);
    }

    public final BooleanProperty showGridLinesProperty() {
        return showGridLines;
    }

    public final boolean isShowOutLines() {
        return showOutLines.get();
    }

    public final void setShowOutLines(boolean showOutLines) {
        this.showOutLines.set(showOutLines);
    }

    public final BooleanProperty showOutLinesProperty() {
        return showOutLines;
    }

    public final double getTokenHeight() {
        return tokenHeight.get();
    }

    public final void setTokenHeight(double tokenHeight) {
        this.tokenHeight.set(tokenHeight);
    }

    public final DoubleProperty tokenHeightProperty() {
        return tokenHeight;
    }

    public final double getTokenWidth() {
        return tokenWidth.get();
    }

    public final void setTokenWidth(double tokenWidth) {
        this.tokenWidth.set(tokenWidth);
    }

    public final DoubleProperty tokenWidthProperty() {
        return tokenWidth;
    }

    public final double getWidth() {
        return width.get();
    }

    public final void setWidth(double width) {
        this.width.set(width);
    }

    public final DoubleProperty widthProperty() {
        return width;
    }
}
