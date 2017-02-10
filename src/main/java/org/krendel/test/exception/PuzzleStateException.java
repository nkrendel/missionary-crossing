package org.krendel.test.exception;

/**
 * An exception representing a problem in the puzzle state.
 */
public class PuzzleStateException extends RuntimeException {
    public PuzzleStateException(String msg) {
        super(msg);
    }
}
