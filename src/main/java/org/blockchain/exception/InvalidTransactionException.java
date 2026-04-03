package org.blockchain.exception;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTransactionException(String message) {
        super(message);
    }
}
