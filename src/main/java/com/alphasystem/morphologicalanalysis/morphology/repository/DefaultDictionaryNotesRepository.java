package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author sali
 */
@Service
public class DefaultDictionaryNotesRepository implements DictionaryNotesRepository {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private static Query getByFileNameQuery(DictionaryNotes dictionaryNotes) {
        return new Query(Criteria.where("filename").is(dictionaryNotes.getFileName()));
    }

    @Override
    public DictionaryNotes store(DictionaryNotes dictionaryNotes) {
        GridFSDBFile gridFSFile = gridFsTemplate.findOne(getByFileNameQuery(dictionaryNotes));
        if (gridFSFile != null) {
            // file already exists, delete first
            delete(dictionaryNotes);
        }
        final GridFSFile file = gridFsTemplate.store(dictionaryNotes.getInputStream(), dictionaryNotes.getFileName(), "plain/text");
        dictionaryNotes.setId(file.getId().toString());
        return dictionaryNotes;
    }

    @Override
    public DictionaryNotes retrieve(RootLetters rootLetters) {
        DictionaryNotes dictionaryNotes = new DictionaryNotes(rootLetters);
        final GridFSDBFile file = gridFsTemplate.findOne(getByFileNameQuery(dictionaryNotes));
        if (file == null) {
            // no dictionary notes for these root letters
            return dictionaryNotes;
        }

        dictionaryNotes.setId(file.getId().toString());
        dictionaryNotes.setInputStream(file.getInputStream());
        return dictionaryNotes;
    }

    @Override
    public void delete(DictionaryNotes dictionaryNotes) {
        gridFsTemplate.delete(getByFileNameQuery(dictionaryNotes));
    }
}
