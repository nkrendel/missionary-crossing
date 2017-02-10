package org.krendel.test;

import org.junit.Test;
import org.krendel.test.model.Move;
import org.krendel.test.model.PuzzleState;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.krendel.test.Solver.CANNIBAL;
import static org.krendel.test.Solver.MISSIONARY;

/**
 * Test some solver algorithms
 */
public class SolverTest {

    /**
     * Test that the apply method produces the expected result
     */
    @Test
    public void testThatApplyWorks() {
        Solver solver = new Solver();
        PuzzleState state = new PuzzleState(Arrays.asList(CANNIBAL, MISSIONARY, CANNIBAL),
                Arrays.asList(MISSIONARY, MISSIONARY, CANNIBAL), PuzzleState.Position.LEFT_BANK);
        Move move = new Move(Move.Direction.LEFT_TO_RIGHT, Arrays.asList(CANNIBAL));

        // apply the move
        PuzzleState newState = solver.apply(state, move);

        PuzzleState expectedState = new PuzzleState(Arrays.asList(MISSIONARY, CANNIBAL),
                Arrays.asList(MISSIONARY, MISSIONARY, CANNIBAL, CANNIBAL), PuzzleState.Position.RIGHT_BANK);
        assertEquals("Expected state not achieved!", expectedState, newState);
    }

    /**
     * Test that the getAvailableMoves method produces the correct moves
     */
    @Test
    public void testThatCorrectMovesAreFound() {
        Solver solver = new Solver();
        PuzzleState state = new PuzzleState(Arrays.asList(CANNIBAL, CANNIBAL),
                Arrays.asList(MISSIONARY, MISSIONARY, CANNIBAL, MISSIONARY), PuzzleState.Position.LEFT_BANK);

        // get available moves
        List<Move> moves = solver.getAvailableMoves(state);

        Move expectedMove = new Move(Move.Direction.LEFT_TO_RIGHT, Arrays.asList(CANNIBAL, CANNIBAL));
        assertEquals("Only two moves should be available!", 2, moves.size());
        assertTrue("List of moves doesn't contain exptected move!", moves.contains(expectedMove));
    }
}
