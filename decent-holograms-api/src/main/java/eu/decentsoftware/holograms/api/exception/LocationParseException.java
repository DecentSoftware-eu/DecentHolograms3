package eu.decentsoftware.holograms.api.exception;

/**
 * This exception is thrown when a location could not be parsed.
 *
 * @author d0by
 */
public class LocationParseException extends Exception {

    public LocationParseException(String message) {
        super(message);
    }

}