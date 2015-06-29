package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static com.alphasystem.util.AppUtil.isGivenType;
import static com.alphasystem.util.HashCodeUtil.hash;
import static com.alphasystem.util.IdGenerator.nextId;

/**
 * @author sali
 */
public abstract class GraphNode implements Externalizable, Comparable<GraphNode> {

    private static final long serialVersionUID = -3432601105627610562L;

    protected final StringProperty id;

    protected final ObjectProperty<GraphNodeType> nodeType;

    protected final StringProperty text;

    protected final DoubleProperty x;

    protected final DoubleProperty y;

    protected final DoubleProperty translateX;

    protected final DoubleProperty translateY;

    /**
     *
     */
    protected GraphNode() {
        this(null, nextId(), null, 0d, 0d);
    }

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     */
    protected GraphNode(GraphNodeType nodeType, String id, String text, Double x, Double y) {
        this(nodeType, id, text, x, y, 0.0, 0.0);
    }

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     * @param translateX
     * @param translateY
     */
    protected GraphNode(GraphNodeType nodeType, String id, String text, Double x, Double y,
                        Double translateX, Double translateY) {
        this.id = new SimpleStringProperty(id);
        this.nodeType = new ReadOnlyObjectWrapper<>(nodeType);
        this.text = new SimpleStringProperty(text);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.translateX = new SimpleDoubleProperty(translateX);
        this.translateY = new SimpleDoubleProperty(translateY);
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

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public final StringProperty idProperty() {
        return id;
    }

    public GraphNodeType getNodeType() {
        return nodeType.get();
    }

    protected void setNodeType(GraphNodeType nodeType) {
        this.nodeType.set(nodeType);
    }

    public final ObjectProperty<GraphNodeType> nodeTypeProperty() {
        return nodeType;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public double getX() {
        return x.get();
    }

    public void setX(Double x) {
        this.x.set(x);
    }

    public final DoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public void setY(Double y) {
        this.y.set(y);
    }

    public final DoubleProperty yProperty() {
        return y;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getId());
        out.writeObject(getNodeType());
        out.writeObject(getText());
        out.writeDouble(getX());
        out.writeDouble(getY());
        out.writeDouble(getTranslateX());
        out.writeDouble(getTranslateY());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setId((String) in.readObject());
        setNodeType((GraphNodeType) in.readObject());
        setText((String) in.readObject());
        setX(in.readDouble());
        setY(in.readDouble());
        setTranslateX(in.readDouble());
        setTranslateY(in.readDouble());
    }

    @Override
    public int hashCode() {
        return hash(super.hashCode(), getId());
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        if (isGivenType(getClass(), obj)) {
            GraphNode gn = (GraphNode) obj;
            result = gn.getId().equals(getId());
        }
        return result;
    }

    @Override
    public int compareTo(GraphNode o) {
        int result = 0;
        if (o == null) {
            result = 1;
        } else {
            String id1 = getId();
            String id2 = o.getId();
            if (id1 == null && id2 == null) {
                result = 0;
            } else if (id2 == null) {
                result = 1;
            } else if (id1 == null) {
                result = -1;
            } else {
                result = id1.compareTo(id2);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return getText();
    }
}
