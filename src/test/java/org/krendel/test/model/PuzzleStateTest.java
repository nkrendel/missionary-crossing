package org.krendel.test.model;

import org.junit.Test;
import org.krendel.test.exception.PuzzleStateException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.krendel.test.Solver.CANNIBAL;
import static org.krendel.test.Solver.MISSIONARY;

/**
 * Test some basic features of the puzzle state class.
 */
public class PuzzleStateTest {

    /**
     * Test that an invalid state is recognized when there are more than 3 cannibals
     */
    @Test(expected = PuzzleStateException.class)
    public void testTooManyCannibals() {
        PuzzleState state = new PuzzleState(Arrays.asList(CANNIBAL, MISSIONARY, CANNIBAL),
                Arrays.asList(MISSIONARY, CANNIBAL, CANNIBAL), PuzzleState.Position.LEFT_BANK);
        assertFalse(state.isValid());
    }

    /**
     * Test that an invalid state is recognized when there are more than 6 actors
     */
    @Test(expected = PuzzleStateException.class)
    public void testTooManyActors() {
        PuzzleState state = new PuzzleState(Arrays.asList(CANNIBAL, MISSIONARY, CANNIBAL),
                Arrays.asList(MISSIONARY, CANNIBAL, MISSIONARY, CANNIBAL), PuzzleState.Position.LEFT_BANK);
        assertFalse(state.isValid());
    }

    /**
     * Test that an invalid state is recognized when there are move cannibals than missionaries
     */
    @Test
    public void testDeadMissionaries() {
        PuzzleState state = new PuzzleState(Arrays.asList(CANNIBAL, MISSIONARY, CANNIBAL),
                Arrays.asList(MISSIONARY, CANNIBAL, MISSIONARY), PuzzleState.Position.LEFT_BANK);
        assertFalse(state.isValid());
    }

    /**
     * Test that the equals method works
     */
    @Test
    public void testEquals()
    {
        PuzzleState state1 = new PuzzleState(Arrays.asList(CANNIBAL, MISSIONARY, CANNIBAL),
                                    Arrays.asList(MISSIONARY, MISSIONARY, CANNIBAL), PuzzleState.Position.LEFT_BANK);
        PuzzleState state2 = new PuzzleState(Arrays.asList(CANNIBAL, CANNIBAL, MISSIONARY),
                                    Arrays.asList(CANNIBAL, MISSIONARY, MISSIONARY), PuzzleState.Position.LEFT_BANK);

        assertEquals("the two puzzle states should be equal!", state1, state2);
    }
}
