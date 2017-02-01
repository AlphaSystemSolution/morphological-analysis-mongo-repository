package com.alphasystem.morphologicalanalysis.spring.support;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author sali
 */
public abstract class SpringContextHelper {

    private static SpringContextHelper instance;

    protected AnnotationConfigApplicationContext context;

    public static synchronized SpringContextHelper getInstance() {
        if (instance == null) {
            ServiceLoader<SpringContextHelper> serviceLoader = ServiceLoader.load(SpringContextHelper.class);
            final Iterator<SpringContextHelper> iterator = serviceLoader.iterator();
            while (iterator.hasNext()) {
                instance = iterator.next();
                if (instance != null) {
                    break;
                }
            }
        }
        if (instance == null) {
            instance = new DefaultSpringContextHelper();
        }
        System.out.println("Current instance is: " + instance.getClass().getName());
        return instance;
    }

    protected SpringContextHelper(Class<?>[] configClasses) {
        if (ArrayUtils.isEmpty(configClasses)) {
            throw new RuntimeException("No configuration classes provided to start Spring context.");
        }
        this.context = new AnnotationConfigApplicationContext(configClasses);
    }

    public AnnotationConfigApplicationContext getContext() {
        if (context == null) {
            throw new RuntimeException("Sprint ApplicationContext hasn't been initialized yet.");
        }
        return context;
    }
}
