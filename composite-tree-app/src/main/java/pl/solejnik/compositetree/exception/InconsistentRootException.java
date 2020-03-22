package pl.solejnik.compositetree.exception;

/**
 * Exception which tels that the given root is inconsistent which this one in db
 */
public class InconsistentRootException extends RuntimeException {

    public InconsistentRootException() {
        super("The root component is inconsistent");
    }
}
