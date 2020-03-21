package pl.solejnik.compositetree.exception;

public class InconsistentRootException extends RuntimeException {

    public InconsistentRootException() {
        super("The root component is inconsistent");
    }
}
