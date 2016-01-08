package com.alphasystem.morphologicalanalysis.repository.export;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.alphasystem.util.AppUtil.NEW_LINE;
import static java.lang.String.format;

/**
 * @author sali
 */
public final class PropertyInfo {

    private final String propertyName;
    private final Field field;
    private final Method readMethod;
    private final Method writeMethod;

    public PropertyInfo(String propertyName, Field field, Method readMethod, Method writeMethod) {
        this.propertyName = propertyName;
        this.field = field;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Field getField() {
        return field;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    @Override
    public String toString() {
        return format("{\"property-name\": \"%s\"%s,\"read-method-name\": \"%s\"%s,\"write-method-name\": \"%s\"}",
                propertyName, NEW_LINE, readMethod.getName(), NEW_LINE, writeMethod.getName());
    }
}
