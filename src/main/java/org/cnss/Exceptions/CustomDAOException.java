package org.cnss.Exceptions;

public class CustomDAOException extends Exception {
    public CustomDAOException(String message) {
        super(message);
    }

    public CustomDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}