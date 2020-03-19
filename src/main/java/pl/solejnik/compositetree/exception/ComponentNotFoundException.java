package pl.solejnik.compositetree.exception;

public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(final Long id) {
        super(String.format("Component with the given %d", id));
    }
}
