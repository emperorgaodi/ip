package darwin;

/**
 * Represents a custom exception specific to the Darwin application. A <code>DarwinException</code>
 * object is thrown when application-specific errors occur, such as invalid user input,
 * file parsing errors, or task management issues.
 */
public class DarwinException extends RuntimeException {
    /**
     * Constructs a new DarwinException with the specified detail message.
     *
     * @param message The detail message describing the specific error condition.
     */
    public DarwinException(String message) {
        super(message);
    }
}
