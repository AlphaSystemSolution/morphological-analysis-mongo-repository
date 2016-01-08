package com.alphasystem.morphologicalanalysis.repository.export;

import org.springframework.data.annotation.Transient;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.err;
import static org.springframework.util.ReflectionUtils.findField;

/**
 * @author sali
 */
public final class ReflectionUtility {

    public static List<PropertyInfo> readProperties(Class<?> _class) {
        List<PropertyInfo> result = new ArrayList<>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(_class, Object.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if (beanInfo != null) {
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                Field field = findField(_class, name, null);
                if (field != null) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    boolean nonTransient = !field.isAnnotationPresent(Transient.class) ||
                            !readMethod.isAnnotationPresent(Transient.class);
                    if (nonTransient) {
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        result.add(new PropertyInfo(name, field, readMethod, writeMethod));
                    } else {
                        err.println(format("Field \"%s\" is transient.", name));
                    }
                } else {
                    err.println(format("Field \"%s\" not found.", name));
                }
            }
        }
        return result;
    }
}
