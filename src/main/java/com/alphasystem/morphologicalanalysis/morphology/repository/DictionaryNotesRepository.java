package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;

/**
 * @author sali
 */
public interface DictionaryNotesRepository {

    /**
     * Stores the dictionary notes related to given {@link RootLetters} into database. If the notes already exists
     * in the database then this method will replace them.
     *
     * @param dictionaryNotes given root letters
     * @return
     */
    DictionaryNotes store(DictionaryNotes dictionaryNotes);

    /**
     * @param rootLetters given root letters
     * @return path representing the notes
     */
    DictionaryNotes retrieve(RootLetters rootLetters);

    /**
     * @param dictionaryNotes
     */
    void delete(DictionaryNotes dictionaryNotes);
}
