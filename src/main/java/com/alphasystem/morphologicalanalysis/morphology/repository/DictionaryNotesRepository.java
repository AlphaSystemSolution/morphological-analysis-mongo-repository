package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.alphasystem.util.AppUtil.USER_HOME_DIR;

/**
 * @author sali
 */
public interface DictionaryNotesRepository {

    String DEFAULT_EXTENSION = ".adoc";
    File DEFAULT_FOLDER = new File(".wordbyword", "dictionary");
    File DEFAULT_NOTES_STORAGE = new File(USER_HOME_DIR, DEFAULT_FOLDER.getPath());

    /**
     * Stores the dictionary notes related to given {@link RootLetters} into database. If the notes already exists
     * in the database then this method will replace them.
     *
     * @param rootLetters given root letters
     * @return String representing the file name of notes
     * @throws IOException
     */
    Path store(RootLetters rootLetters) throws IOException;

    /**
     * @param rootLetters given root letters
     * @return path representing the notes
     */
    Path retrieve(RootLetters rootLetters) throws IOException;

    /**
     * @param rootLetters
     */
    void delete(RootLetters rootLetters);
}
