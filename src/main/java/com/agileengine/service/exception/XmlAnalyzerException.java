package com.agileengine.service.exception;

public class XmlAnalyzerException extends RuntimeException {

    public XmlAnalyzerException() {
    }

    public XmlAnalyzerException(String message) {
        super(message);
    }

    public XmlAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlAnalyzerException(Throwable cause) {
        super(cause);
    }

    public XmlAnalyzerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
