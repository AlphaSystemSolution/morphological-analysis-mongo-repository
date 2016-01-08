package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.repository.export.PropertyInfo;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.persistence.model.AbstractDocument;
import com.alphasystem.persistence.model.AbstractSimpleDocument;
import com.alphasystem.persistence.mongo.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.repository.export.ReflectionUtility.readProperties;
import static com.alphasystem.util.AppUtil.USER_HOME_DIR;
import static com.alphasystem.util.AppUtil.isGivenType;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static org.testng.Assert.assertTrue;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class DataExportUtility extends AbstractTestNGSpringContextTests {

    private static final int PAGE_SIZE = 50;
    private static final File EXPORT_ROOT_FOLDER = new File(USER_HOME_DIR, ".wordbyword");
    private static final File EXPORT_FOLDER = new File(EXPORT_ROOT_FOLDER, "export");

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    private static String addPropertyNames(List<PropertyInfo> propertyInfoList) {
        StringBuilder builder = new StringBuilder();
        builder.append(propertyInfoList.get(0).getPropertyName());
        for (int i = 1; i < propertyInfoList.size(); i++) {
            PropertyInfo propertyInfo = propertyInfoList.get(i);
            builder.append(",").append(propertyInfo.getPropertyName());
        }
        return builder.toString();
    }

    private static void saveFile(File exportFile, List<String> lines) {
        try {
            write(get(exportFile.getAbsolutePath()), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void beforeSuite() {
        log("Checking database", true);
        String dbName = getProperty(MongoConfig.MONGO_DB_NAME_PROPERTY);
        log(format("Database in use {%s}", dbName), true);
        assertTrue("MORPHOLOGICAL_ANALYSIS_DB".equals(dbName));
        EXPORT_FOLDER.mkdirs();
    }

    @Test
    public void exportData() {
        export(MorphologicalEntry.class, repositoryUtil.getMorphologicalEntryRepository());

        log("Done", true);
    }

    private <T extends AbstractDocument> void export(Class<T> _class, BaseRepository<T> repository) {
        log(format("Exporting class \"%s\"", _class.getName()), true);
        List<PropertyInfo> propertyInfoList = readProperties(_class);
        File exportFile = new File(EXPORT_FOLDER, format("%s.csv", _class.getName()));
        List<String> lines = new ArrayList<>();
        lines.add(addPropertyNames(propertyInfoList));

        long startTime = System.currentTimeMillis();
        addRepository(_class, repository, propertyInfoList, lines);
        long endTime = System.currentTimeMillis();
        log(format("Total time to export document \"%s\" is \"%s\"", _class.getSimpleName(),
                (endTime - startTime)), true);
        saveFile(exportFile, lines);
        log(format("Finished exporting class \"%s\"", _class.getName()), true);
    }

    private <T extends AbstractDocument> void addRepository(Class<T> _class, BaseRepository<T> repository,
                                                            List<PropertyInfo> propertyInfoList, List<String> lines) {
        int currentPage = 0;
        while (true) {
            long startOfIteration = System.currentTimeMillis();
            Page<T> page = repository.findAll(new PageRequest(currentPage, PAGE_SIZE));
            List<T> content = page.getContent();
            lines.addAll(addContent(propertyInfoList, content));
            long endOfIteration = System.currentTimeMillis();
            log(format("Total time to export iteration \"%s\" for document \"%s\" is \"%s\"", currentPage,
                    _class.getSimpleName(), (endOfIteration - startOfIteration)), true);
            if (!page.hasNext()) {
                break;
            }
            currentPage = page.getNumber() + 1;
        }
    }

    private <T extends AbstractDocument> List<String> addContent(List<PropertyInfo> propertyInfoList, List<T> content) {
        List<String> lines = new ArrayList<>();
        if (content != null && !content.isEmpty()) {
            content.forEach(t -> {
                lines.add(addDocument(propertyInfoList, t));
            });
        }
        return lines;
    }

    private <T extends AbstractDocument> String addDocument(List<PropertyInfo> propertyInfoList, T t) {
        StringBuilder builder = new StringBuilder();
        PropertyInfo propertyInfo = propertyInfoList.get(0);
        builder.append(addField(propertyInfo, t));
        for (int i = 1; i < propertyInfoList.size(); i++) {
            propertyInfo = propertyInfoList.get(i);
            builder.append(",").append(addField(propertyInfo, t));
        }
        return builder.toString();
    }

    private <T extends AbstractDocument> String addField(PropertyInfo propertyInfo, T t) {
        Field field = propertyInfo.getField();
        Class<?> fieldType = field.getType();
        Object fieldValue = ReflectionUtils.getField(field, t);
        String value = null;
        String propertyName = propertyInfo.getPropertyName();
        if (fieldValue != null) {
            if (isGivenType(AbstractSimpleDocument.class, fieldValue)) {
                value = handleDocument((AbstractSimpleDocument) fieldValue, propertyInfo);
            } else if (isGivenType(Collection.class, fieldValue)) {
                value = handleCollection(fieldValue, propertyInfo);
            } else if (fieldType.isEnum()) {
                value = handleEnum(fieldValue);
            } else if (fieldType.isPrimitive()) {
                log(format("Property \"%s\" is of primitive type", propertyName), true);
            } else if (fieldType.isArray()) {
                log(format("Property \"%s\" is of array type", propertyName), true);
            } else if (isGivenType(String.class, fieldValue)) {
                value = (String) fieldValue;
            } else {
                log(format("Unhandled property \"%s\" of type \"%s\"", propertyName, fieldType.getSimpleName()), true);
            }
        }
        return value;
    }

    private String handleCollection(Object fieldValue, PropertyInfo propertyInfo) {
        Collection<?> collection = (Collection<?>) fieldValue;
        List<String> values = new ArrayList<>();
        for (Object o : collection) {
            if (isGivenType(AbstractSimpleDocument.class, o)) {
                values.add(handleDocument((AbstractSimpleDocument) o, propertyInfo));
            } else if (o.getClass().isEnum()) {
                values.add(handleEnum(o));
            } else if (isGivenType(String.class, o)) {
                values.add((String) o);
            } else {
                log(format("Unhandled property \"%s\" of type \"%s\"", propertyInfo.getPropertyName(),
                        fieldValue.getClass().getSimpleName()), true);
            }
        }
        if (values.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            builder.append(":").append(values.get(i));
        }
        return builder.toString();
    }

    private String handleEnum(Object fieldValue) {
        Enum<?> _enum = (Enum<?>) fieldValue;
        return _enum.name();
    }

    private String handleDocument(AbstractSimpleDocument fieldValue, PropertyInfo propertyInfo) {
        String value = null;
        if (propertyInfo.getField().isAnnotationPresent(DBRef.class)) {
            value = fieldValue.getId();
        } else {
            log(format("Need to handle embedded document \"%s\"", propertyInfo.getPropertyName()), true);
        }
        return value;
    }


}
