package NPArray;

public class ArrayShapeException extends Exception {
    public ArrayShapeException(String message) {
        super(message);
    }

    ArrayShapeException(String message, Throwable err) {
        super(message, err);
    }
}
