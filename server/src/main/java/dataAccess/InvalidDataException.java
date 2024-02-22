package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class InvalidDataException extends Exception{
    public InvalidDataException(String message) {
        super(message);
    }
}
