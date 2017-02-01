package com.alphasystem.morphologicalanalysis.util;

/**
 * @author sali
 */
public enum Script {

    SIMPLE("Simple"),
    SIMPLE_CLEAN("Simple (Clean)"),
    SIMPLE_ENHANCED("Simple (Enhanced)"),
    SIMPLE_MIN("Simple (Minimum)"),
    UTHMANI("Uthmani"),
    UTHMANI_MIN("Uthmani (Minimum)");

    private final String path;
    private final String dbName;
    private final String description;

    Script(String description) {
        this.path = String.format("tanzil/quran-%s.xml", name().replaceAll("_", "-"));
        this.dbName = name();
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDescription() {
        return description;
    }

}
