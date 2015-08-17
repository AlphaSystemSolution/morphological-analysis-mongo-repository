package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import javafx.beans.property.*;
import javafx.scene.text.Font;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static javafx.scene.text.Font.font;

/**
 * Bean to capture canvas meta data.
 *
 * @author sali
 */
public class CanvasMetaData implements Externalizable {

    private final IntegerProperty width;
    private final IntegerProperty height;
    private final DoubleProperty tokenWidth;
    private final DoubleProperty tokenHeight;
    private final DoubleProperty gapBetweenTokens;
    private final BooleanProperty showGridLines;
    private final BooleanProperty showOutLines;
    private final BooleanProperty debugMode;
    private final ObjectProperty<Font> tokenFont;
    private final ObjectProperty<Font> partOfSpeechFont;
    private final ObjectProperty<Font> translationFont;

    public CanvasMetaData() {
        this(800, 400, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, GAP_BETWEEN_TOKENS, true, true, false,
                ARABIC_FONT_NAME, DEFAULT_TOKEN_FONT_SIZE, DEFAULT_POS_FONT_SIZE,
                TRANSLATION_FONT_FAMILY, DEFAULT_TRANSLATION_FONT_SIZE);
    }

    public CanvasMetaData(int width, int height, double tokenWidth, double tokenHeight, double gapBetweenTokens,
                          boolean showGridLines, boolean showOutLines, boolean debugMode, String fontFamily,
                          int tokenFontSize, int partOfSpeechFontSize,
                          String translationFontFamily, double translationFontSize) {
        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
        this.tokenWidth = new SimpleDoubleProperty(tokenWidth);
        this.tokenHeight = new SimpleDoubleProperty(tokenHeight);
        this.gapBetweenTokens = new SimpleDoubleProperty(gapBetweenTokens);
        this.showGridLines = new SimpleBooleanProperty(showGridLines);
        this.showOutLines = new SimpleBooleanProperty(showOutLines);
        this.debugMode = new SimpleBooleanProperty(debugMode);

        this.tokenFont = new SimpleObjectProperty<>();
        setTokenFont(font(fontFamily, tokenFontSize));

        this.partOfSpeechFont = new SimpleObjectProperty<>();
        setPartOfSpeechFont(font(fontFamily, partOfSpeechFontSize));

        this.translationFont = new SimpleObjectProperty<>();
        setTranslationFont(font(translationFontFamily, translationFontSize));
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

    public int getHeight() {
        return height.get();
    }

    public void setHeight(int height) {
        this.height.set(height);
    }

    public final IntegerProperty heightProperty() {
        return height;
    }

    public final double getTokenWidth() {
        return tokenWidth.get();
    }

    public final DoubleProperty tokenWidthProperty() {
        return tokenWidth;
    }

    public final void setTokenWidth(double tokenWidth) {
        this.tokenWidth.set(tokenWidth);
    }

    public final double getTokenHeight() {
        return tokenHeight.get();
    }

    public final DoubleProperty tokenHeightProperty() {
        return tokenHeight;
    }

    public final void setTokenHeight(double tokenHeight) {
        this.tokenHeight.set(tokenHeight);
    }

    public final double getGapBetweenTokens() {
        return gapBetweenTokens.get();
    }

    public final DoubleProperty gapBetweenTokensProperty() {
        return gapBetweenTokens;
    }

    public final void setGapBetweenTokens(double gapBetweenTokens) {
        this.gapBetweenTokens.set(gapBetweenTokens);
    }

    public boolean isShowGridLines() {
        return showGridLines.get();
    }

    public void setShowGridLines(boolean showGridLines) {
        this.showGridLines.set(showGridLines);
    }

    public final BooleanProperty showGridLinesProperty() {
        return showGridLines;
    }

    public boolean isShowOutLines() {
        return showOutLines.get();
    }

    public void setShowOutLines(boolean showOutLines) {
        this.showOutLines.set(showOutLines);
    }

    public final BooleanProperty showOutLinesProperty() {
        return showOutLines;
    }

    public int getWidth() {
        return width.get();
    }

    public void setWidth(int width) {
        this.width.set(width);
    }

    public IntegerProperty widthProperty() {
        return width;
    }

    public final Font getTokenFont() {
        return tokenFont.get();
    }

    public final ObjectProperty<Font> tokenFontProperty() {
        return tokenFont;
    }

    public final void setTokenFont(Font tokenFont) {
        this.tokenFont.set(tokenFont);
    }

    public final Font getPartOfSpeechFont() {
        return partOfSpeechFont.get();
    }

    public final ObjectProperty<Font> partOfSpeechFontProperty() {
        return partOfSpeechFont;
    }

    public final void setPartOfSpeechFont(Font partOfSpeechFont) {
        this.partOfSpeechFont.set(partOfSpeechFont);
    }

    public final Font getTranslationFont() {
        return translationFont.get();
    }

    public final ObjectProperty<Font> translationFontProperty() {
        return translationFont;
    }

    public final void setTranslationFont(Font translationFont) {
        this.translationFont.set(translationFont);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getWidth());
        out.writeInt(getHeight());
        out.writeBoolean(isShowGridLines());
        out.writeBoolean(isShowOutLines());
        out.writeBoolean(isDebugMode());
        out.writeObject(getTokenFont().getFamily());
        out.writeDouble(getTokenFont().getSize());
        out.writeObject(getPartOfSpeechFont().getFamily());
        out.writeDouble(getPartOfSpeechFont().getSize());
        out.writeObject(getTranslationFont().getFamily());
        out.writeDouble(getTranslationFont().getSize());
        out.writeDouble(getTokenWidth());
        out.writeDouble(getTokenHeight());
        out.writeDouble(getGapBetweenTokens());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setWidth(in.readInt());
        setHeight(in.readInt());
        setShowGridLines(in.readBoolean());
        setShowOutLines(in.readBoolean());
        setDebugMode(in.readBoolean());
        //TODO: uncomment this
        setTokenFont(createFont(in));
        setPartOfSpeechFont(createFont(in));
        setTranslationFont(createFont(in));
        setTokenWidth(in.readDouble());
        setTokenHeight(in.readDouble());
        setGapBetweenTokens(in.readDouble());
    }

    private Font createFont(ObjectInput in) throws IOException, ClassNotFoundException {
        return font((String) in.readObject(), in.readDouble());
    }
}
