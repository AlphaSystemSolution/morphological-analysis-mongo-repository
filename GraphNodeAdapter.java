package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import javafx.beans.property.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author sali
 */
public abstract class GraphNodeAdapter<N extends GraphNode> {

    private final ObjectProperty<N> src = new SimpleObjectProperty<>();
    private final StringProperty id = new SimpleStringProperty();
    private final ObjectProperty<GraphNodeType> graphNodeType = new SimpleObjectProperty<>();
    private final StringProperty text = new SimpleStringProperty();
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty translateX = new SimpleDoubleProperty();
    private final DoubleProperty translateY = new SimpleDoubleProperty();
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>();
    private Object parent;

    protected GraphNodeAdapter() {
        srcProperty().addListener((observable, oldValue, newValue) -> {
            initValues(newValue);
        });
        xProperty().addListener((observable, oldValue, newValue) -> getSrc().setX((Double) newValue));
        yProperty().addListener((observable, oldValue, newValue) -> getSrc().setY((Double) newValue));
        translateXProperty().addListener((observable, oldValue, newValue) -> getSrc().setTranslateX((Double) newValue));
        translateYProperty().addListener((observable, oldValue, newValue) -> getSrc().setTranslateY((Double) newValue));
        fontProperty().addListener((observable, oldValue, newValue) ->
                getSrc().getFont().withFamily(newValue.getFamily()).withSize(newValue.getSize()));
    }

    protected void initValues(N graphNode) {
        setId(graphNode == null ? null : graphNode.getId());
        setGraphNodeType(graphNode == null ? null : graphNode.getGraphNodeType());
        setText(CanvasUtil.getInstance().getNodeText(graphNode));
        setX(graphNode == null ? null : graphNode.getX());
        setY(graphNode == null ? null : graphNode.getY());
        setTranslateX(graphNode == null ? null : graphNode.getTranslateX());
        setTranslateY(graphNode == null ? null : graphNode.getTranslateY());
        Font font = null;
        if (graphNode != null) {
            FontMetaInfo fontMetaInfo = graphNode.getFont();
            font = Font.font(fontMetaInfo.getFamily(), FontWeight.valueOf(fontMetaInfo.getWeight()),
                    FontPosture.valueOf(fontMetaInfo.getPosture()), fontMetaInfo.getSize());
        }
        setFont(font);
    }

    public final String getId() {
        return id.get();
    }

    public final void setId(String id) {
        this.id.set(id);
    }

    public final StringProperty idProperty() {
        return id;
    }

    public final N getSrc() {
        return src.get();
    }

    public final void setSrc(N src) {
        this.src.set(src);
    }

    public final ObjectProperty<N> srcProperty() {
        return src;
    }

    public final GraphNodeType getGraphNodeType() {
        return graphNodeType.get();
    }

    public final void setGraphNodeType(GraphNodeType graphNodeType) {
        this.graphNodeType.set(graphNodeType);
    }

    public final ObjectProperty<GraphNodeType> graphNodeTypeProperty() {
        return graphNodeType;
    }

    public final double getX() {
        return x.get();
    }

    public final void setX(double x) {
        this.x.set(x);
    }

    public final DoubleProperty xProperty() {
        return x;
    }

    public final double getY() {
        return y.get();
    }

    public final void setY(double y) {
        this.y.set(y);
    }

    public final DoubleProperty yProperty() {
        return y;
    }

    public final double getTranslateX() {
        return translateX.get();
    }

    public final void setTranslateX(double translateX) {
        this.translateX.set(translateX);
    }

    public final DoubleProperty translateXProperty() {
        return translateX;
    }

    public final double getTranslateY() {
        return translateY.get();
    }

    public final void setTranslateY(double translateY) {
        this.translateY.set(translateY);
    }

    public final DoubleProperty translateYProperty() {
        return translateY;
    }

    public final Font getFont() {
        return font.get();
    }

    public final void setFont(Font font) {
        this.font.set(font);
    }

    public final ObjectProperty<Font> fontProperty() {
        return font;
    }

    public final String getText() {
        return text.get();
    }

    public final void setText(String text) {
        this.text.set(text);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getSrc() == null ? super.toString() : getSrc().getDisplayName();
    }
}
