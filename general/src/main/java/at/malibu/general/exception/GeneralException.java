package at.malibu.general.exception;

import lombok.NonNull;

public class GeneralException extends RuntimeException {

    /**
     * @param message the message that explains the issue
     */
    public GeneralException(@NonNull String message) {
        super(message);
    }

    /**
     * @param message the message that explains the issue
     * @param e       the root exception that caused this exception
     */
    public GeneralException(@NonNull String message, @NonNull Exception e) {
        super(message, e);
    }
}