package org.epics.etherip.exceptions;

/**
 * 12/16/16
 *
 * @author <a href="mailto:cepasp@rit.edu">Christopher Piggott</a>
 *         Rochester Institute of Technology
 */
public class DecodingException extends Exception {
    public DecodingException() {
        super();
    }

    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodingException(Throwable cause) {
        super(cause);
    }

    protected DecodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
