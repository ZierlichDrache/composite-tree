package pl.solejnik.compositetree.exception;

/**
 * Exception which tels that the given component cannot be found
 */
public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(final Long id) {
        super(String.format("Component with the given %d doesn't exists", id));
    }
}
