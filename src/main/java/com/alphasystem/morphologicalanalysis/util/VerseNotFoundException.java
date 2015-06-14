package com.alphasystem.morphologicalanalysis.util;

/**
 * @author sali
 */
public class VerseNotFoundException extends RuntimeException {

    public VerseNotFoundException() {
        super();
    }

    public VerseNotFoundException(String message) {
        super(message);
    }

    public VerseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerseNotFoundException(Throwable cause) {
        super(cause);
    }

    protected VerseNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
