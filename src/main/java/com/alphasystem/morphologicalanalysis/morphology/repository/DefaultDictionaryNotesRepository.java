package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
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

    private static Query getQuery(DictionaryNotes dictionaryNotes) {
        return new Query(Criteria.where("metadata.rootLetters").is(dictionaryNotes.getRootLetters().getDisplayName()));
    }

    @Override
    public DictionaryNotes store(DictionaryNotes dictionaryNotes) {
        GridFSDBFile gridFSFile = gridFsTemplate.findOne(getQuery(dictionaryNotes));
        if (gridFSFile != null) {
            // file already exists, delete first
            delete(dictionaryNotes);
        }
        DBObject metaData = new BasicDBObject();
        final RootLetters rootLetters = dictionaryNotes.getRootLetters();
        metaData.put("rootLetters", rootLetters.getDisplayName());
        metaData.put("firstRadical", rootLetters.getFirstRadical().name());
        metaData.put("secondRadical", rootLetters.getSecondRadical().name());
        metaData.put("thirdRadical", rootLetters.getThirdRadical().name());
        final ArabicLetterType fourthRadical = rootLetters.getFourthRadical();
        metaData.put("fourthRadical", (fourthRadical == null) ? null : fourthRadical.name());
        final GridFSFile file = gridFsTemplate.store(dictionaryNotes.getInputStream(), dictionaryNotes.getFileName(),
                "plain/text", metaData);
        dictionaryNotes.setId(file.getId().toString());
        return dictionaryNotes;
    }

    @Override
    public DictionaryNotes retrieve(RootLetters rootLetters) {
        DictionaryNotes dictionaryNotes = new DictionaryNotes(rootLetters);
        final GridFSDBFile file = gridFsTemplate.findOne(getQuery(dictionaryNotes));
        if (file == null) {
            // no dictionary notes for these root letters
            return dictionaryNotes;
        }

        dictionaryNotes.setId(file.getId().toString());
        dictionaryNotes.setFileName(file.getFilename());
        dictionaryNotes.setInputStream(file.getInputStream());
        return dictionaryNotes;
    }

    @Override
    public void delete(DictionaryNotes dictionaryNotes) {
        gridFsTemplate.delete(getQuery(dictionaryNotes));
    }
}
