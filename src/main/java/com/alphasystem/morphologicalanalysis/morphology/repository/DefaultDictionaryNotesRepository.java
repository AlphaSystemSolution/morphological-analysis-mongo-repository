package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import static com.alphasystem.util.AppUtil.fastCopy;
import static java.lang.String.format;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * @author sali
 */
@Service
public class DefaultDictionaryNotesRepository implements DictionaryNotesRepository {

    static {
        if (!DEFAULT_NOTES_STORAGE.exists()) {
            DEFAULT_NOTES_STORAGE.mkdirs();
        }
    }

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private static Query getByFileNameQuery(RootLetters rootLetters) {
        return new Query(Criteria.where("filename").is(getFileName(rootLetters)));
    }

    private static String getFileName(RootLetters rootLetters) {
        return format("%s%s", rootLetters.getName(), DEFAULT_EXTENSION);
    }

    private static Path getFilePath(RootLetters rootLetters) {
        return get(DEFAULT_NOTES_STORAGE.getPath(), getFileName(rootLetters));
    }

    @Override
    public Path store(RootLetters rootLetters) throws IOException {
        GridFSDBFile gridFSFile = gridFsTemplate.findOne(getByFileNameQuery(rootLetters));
        if (gridFSFile != null) {
            // file already exists, delete first
            delete(rootLetters);
        }
        GridFSFile file;
        final String fileName = getFileName(rootLetters);
        final Path filePath = getFilePath(rootLetters);
        try (InputStream inputStream = newInputStream(filePath)) {
            file = gridFsTemplate.store(inputStream, fileName, "plain/text");
        } catch (IOException e) {
            throw e;
        }
        return filePath;
    }

    @Override
    public Path retrieve(RootLetters rootLetters) throws IOException {
        final GridFSDBFile file = gridFsTemplate.findOne(getByFileNameQuery(rootLetters));
        final Path filePath = getFilePath(rootLetters);
        final OutputStream outputStream = newOutputStream(filePath, WRITE, CREATE);
        fastCopy(file.getInputStream(), outputStream);
        return filePath;
    }

    @Override
    public void delete(RootLetters rootLetters) {
        gridFsTemplate.delete(getByFileNameQuery(rootLetters));
    }
}
