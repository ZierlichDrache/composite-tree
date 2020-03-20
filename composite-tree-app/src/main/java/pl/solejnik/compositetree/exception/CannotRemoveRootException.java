package pl.solejnik.compositetree.exception;

public class CannotRemoveRootException extends RuntimeException {

    public CannotRemoveRootException() {
        super("Cannot remove the root component");
    }
}
