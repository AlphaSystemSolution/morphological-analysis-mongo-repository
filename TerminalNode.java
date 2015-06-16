package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.TERMINAL;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.PART_OF_SPEECH_EXCLUDE_LIST;
import static javafx.collections.FXCollections.observableArrayList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * @author sali
 */
public class TerminalNode extends LineSupport {

    private static final long serialVersionUID = 3916613726425333716L;

    /**
     * x location for translation
     */
    protected final DoubleProperty x3;

    /**
     * y location for translation
     */
    protected final DoubleProperty y3;

    /**
     *
     */
    protected ObjectProperty<Token> token;

    /**
     *
     */
    protected ObservableList<PartOfSpeechNode> partOfSpeeches;

    /**
     *
     */
    public TerminalNode() {
        this(null, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 0.0, 0.0);
    }

    /**
     *
     * @param token
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public TerminalNode(Token token, Double x, Double y, Double x1, Double y1, Double x2,
                        Double y2, Double x3, Double y3) {
        this(token, x, y, x1, y1, x2, y2, x3, y3, 0d, 0d);
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param translateX
     * @param translateY
     */
    public TerminalNode(Token token, Double x, Double y, Double x1, Double y1, Double x2,
                        Double y2, Double x3, Double y3, Double translateX, Double translateY,
                        PartOfSpeechNode... partOfSpeechNodes) {
        this(TERMINAL, token, x, y, x1, y1, x2, y2, x3, y3, translateX, translateY, partOfSpeechNodes);
    }

    /**
     * @param nodeType
     * @param token
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param translateX
     * @param translateY
     */
    protected TerminalNode(GraphNodeType nodeType, Token token, Double x, Double y,
                           Double x1, Double y1, Double x2, Double y2, Double x3, Double y3,
                           Double translateX, Double translateY, PartOfSpeechNode... partOfSpeechNodes) {
        this(nodeType, null, null, x, y, x1, y1, x2, y2, x3, y3, translateX, translateY);
        this.token = new SimpleObjectProperty<>();
        tokenProperty().addListener((observable, oldToken, newToken) -> {
            if (newToken == null) {
                this.partOfSpeeches.remove(0, this.partOfSpeeches.size());
            } else {
                setText(getTokenValue(newToken));
                setId(newToken.getId());
            }
        });
        setToken(token);
        loadPartOfSpeechNodes();
        if (!isEmpty(partOfSpeechNodes)) {
            this.partOfSpeeches.setAll(partOfSpeechNodes);
        }

    }

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param translateX
     * @param translateY
     */
    protected TerminalNode(GraphNodeType nodeType, String id, String text, Double x, Double y,
                           Double x1, Double y1, Double x2, Double y2, Double x3, Double y3,
                           Double translateX, Double translateY) {
        super(nodeType, id, text, x, y, x1, y1, x2, y2, translateX, translateY);
        this.x3 = new SimpleDoubleProperty(x3);
        this.y3 = new SimpleDoubleProperty(y3);
        this.partOfSpeeches = observableArrayList();
    }

    private static String getTokenValue(Token token) {
        return (token == null) ? null : token.getTokenWord().toUnicode();
    }

    private void loadPartOfSpeechNodes() {
        this.partOfSpeeches.remove(0, this.partOfSpeeches.size());
        Token token = getToken();
        if (token == null) {
            return;
        }
        List<Location> locations = token.getLocations();
        if (locations != null && !locations.isEmpty()) {
            for (Location location : locations) {
                PartOfSpeech partOfSpeech = location.getPartOfSpeech();
                if (PART_OF_SPEECH_EXCLUDE_LIST.contains(partOfSpeech)) {
                    continue;
                }
                this.partOfSpeeches.add(new PartOfSpeechNode(location, -1d, -1d, -1d, -1d));
            }
        }
    }

    public ObservableList<PartOfSpeechNode> getPartOfSpeeches() {
        return partOfSpeeches;
    }

    public void setPartOfSpeeches(ObservableList<PartOfSpeechNode> partOfSpeeches) {
        this.partOfSpeeches.remove(0, this.partOfSpeeches.size());
        this.partOfSpeeches.addAll(partOfSpeeches);
    }

    public final Double getX3() {
        return x3.get();
    }

    public final void setX3(Double x3) {
        this.x3.set(x3);
    }

    public final DoubleProperty x3Property() {
        return x3;
    }

    public final Double getY3() {
        return y3.get();
    }

    public final void setY3(Double y3) {
        this.y3.set(y3);
    }

    public final DoubleProperty y3Property() {
        return y3;
    }

    public final Token getToken() {
        return token.get();
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }

    public final ObjectProperty<Token> tokenProperty() {
        return token;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeDouble(getX3());
        out.writeDouble(getY3());
        List<PartOfSpeechNode> nodes = new ArrayList<>();
        if(partOfSpeeches != null && !partOfSpeeches.isEmpty()){
            nodes.addAll(partOfSpeeches);
        }
        out.writeObject(nodes);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setX3(in.readDouble());
        setY3(in.readDouble());
        List<PartOfSpeechNode> nodes = (List<PartOfSpeechNode>) in.readObject();
        partOfSpeeches.setAll(nodes);
    }
}
