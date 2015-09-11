package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import javafx.beans.property.*;

/**
 * @author sali
 */
public class GraphMetaInfoAdapter {

    private final ObjectProperty<GraphMetaInfo> graphMetaInfo = new SimpleObjectProperty<>();
    private final DoubleProperty width = new SimpleDoubleProperty();
    private final DoubleProperty height = new SimpleDoubleProperty();
    private final DoubleProperty tokenWidth = new SimpleDoubleProperty();
    private final DoubleProperty tokenHeight = new SimpleDoubleProperty();
    private final DoubleProperty gapBetweenTokens = new SimpleDoubleProperty();
    private final BooleanProperty showGridLines = new SimpleBooleanProperty();
    private final BooleanProperty showOutLines = new SimpleBooleanProperty();
    private final BooleanProperty debugMode = new SimpleBooleanProperty();

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
        });
        setGraphMetaInfo(src);
        widthProperty().addListener((observable, oldValue, newValue) ->
                getGraphMetaInfo().setWidth((Double) newValue));
        heightProperty().addListener((observable, oldValue, newValue) ->
                getGraphMetaInfo().setHeight((Double) newValue));
        gapBetweenTokensProperty().addListener((observable, oldValue, newValue) ->
                getGraphMetaInfo().setGapBetweenTokens((Double) newValue));
        showGridLinesProperty().addListener((observable, oldValue, newValue) ->
                getGraphMetaInfo().setShowGridLines(newValue));
        showOutLinesProperty().addListener((observable, oldValue, newValue) ->
                getGraphMetaInfo().setShowOutLines(newValue));
        debugModeProperty().addListener((observable, oldValue, newValue) -> getGraphMetaInfo().setDebugMode(newValue));
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
