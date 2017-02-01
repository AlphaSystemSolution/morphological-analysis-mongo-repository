package com.alphasystem.morphologicalanalysis.spring.support;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sali
 */
public class DefaultSpringContextHelper extends SpringContextHelper {

    DefaultSpringContextHelper() {
        this(new Class<?>[0]);
    }

    protected DefaultSpringContextHelper(Class<?>... customConfigClasses) {
        super(getConfigClasses(customConfigClasses));
    }

    private static Class<?>[] getConfigClasses(Class<?>[] customConfigClasses) {
        List<Class<?>> classes = new ArrayList<>();
        Collections.addAll(classes, MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class);
        if (!ArrayUtils.isEmpty(customConfigClasses)) {
            Collections.addAll(classes, customConfigClasses);
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }

}
