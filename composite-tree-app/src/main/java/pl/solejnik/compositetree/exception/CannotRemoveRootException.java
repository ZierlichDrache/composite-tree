package pl.solejnik.compositetree.exception;

/**
 * Exception which tels that the root component cannot be removed from the db
 */
public class CannotRemoveRootException extends RuntimeException {

    public CannotRemoveRootException() {
        super("Cannot remove the root component");
    }
}
