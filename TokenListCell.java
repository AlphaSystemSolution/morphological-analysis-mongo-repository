package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static java.lang.String.format;

/**
 * @author sali
 */
public class TokenListCell {

    private final Token token;
    private final BooleanProperty selected;
    private final StringProperty name;

    public TokenListCell(Token token) {
        this.token = token;
        selected = new SimpleBooleanProperty();
        name = new SimpleStringProperty(format("%s (%s)", token.getTokenWord().toUnicode(),
                token.getDisplayName()));
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public final StringProperty nameProperty() {
        return name;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    @Override
    public String toString() {
        return getName();
    }

}
