package org.krendel.test;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.krendel.test.exception.PuzzleStateException;
import org.krendel.test.model.Move;
import org.krendel.test.model.PuzzleState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Attempt to solve the Missionaries and Cannibals problem using a brute force approach.
 */
public class Solver {
    private final static Logger LOG = Logger.getLogger(Solver.class);

    public static char MISSIONARY = 'M';
    public static char CANNIBAL   = 'C';
    private boolean puzzleSolved  = false;

    List<Pair<PuzzleState, Move>> playedMoves = new ArrayList<>();
    Stack<Pair<PuzzleState, Move>> solution = new Stack<>();

    public void solve() {
        // rough logic:
        // 1. come up with a list of possible moves from the current state (should be max 5 moves)
        // 2. loop through the possible moves
        // 3. for each move, apply the move to the state, check if the resulting state is valid
        // 4. if not valid, undo last move, continue with next move
        // 5. if state is valid, recurse until final state?
        // 6. if iterated through all available moves and all are invalid, back up and continue with next untried move

        PuzzleState initialState = new PuzzleState(Arrays.asList(MISSIONARY, MISSIONARY, MISSIONARY,
                CANNIBAL, CANNIBAL, CANNIBAL), null, PuzzleState.Position.LEFT_BANK);

        // push initial state to the stack
        solution.push(new ImmutablePair<>(initialState, null));

        // try some moves
        tryMoves(initialState);

        // see if we solved it
        if (puzzleSolved) {
            // clean up the solution list by removing "loops"
            Stack<Pair<PuzzleState, Move>> cleanSolution = new Stack<>();
            while (solution.size() > 0) {
                Pair<PuzzleState, Move> pair = solution.pop();
                if (cleanSolution.stream().filter(p -> p.getLeft().equals(pair.getLeft())).count() > 0) {
                    while (!cleanSolution.pop().getLeft().equals(pair.getLeft()))
                        ;
                }
                cleanSolution.push(pair);
            }

            System.out.println("Solution has " + cleanSolution.size() + " moves.");
            while (cleanSolution.size() > 0) {
                Pair<PuzzleState, Move> pair = cleanSolution.pop();
                System.out.println(String.format("(%s) %s", pair.getRight(), pair.getLeft()));
            }
        } else {
            System.out.println("Sorry, unable to solve... :-(");
        }
    }

    /**
     * Recursive method to try to apply a list of moves to the current puzzle state

     * @param state current puzzle state
     */
    private void tryMoves(PuzzleState state) {
        List<Move> availableMoves = getAvailableMoves(state);

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("available moves: %d board state: %s, stack size: %d", availableMoves.size(), state, solution.size()));
        }
        // loop through moves
        for (Move move : availableMoves) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying: " + move);
            }

            if (alreadyPlayed(state, move)) {
                LOG.debug("(already played)");
                continue;
            }

            PuzzleState newState = apply(state, move);
            playedMoves.add(new ImmutablePair<>(state, move));

            if (newState.isValid()) {
                solution.push(new ImmutablePair<>(newState, move));    // add to stack

                if (newState.isFinalState()) {
                    // solved!
                    puzzleSolved = true;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("*** SOLVED!!! *** " + newState);
                    }
                    break; // we need another flag to get out of multiple layers of recursion
                }

                // recurse to try another level of moves
                tryMoves(newState);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("popped back... board state: " + state + " available moves: " + availableMoves.size());
                    LOG.debug("Last state in stack: " + ((solution.size() > 0) ? solution.peek() : "NONE"));
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Move resulted in a state that isn't valid: %s", newState));
                }
                // new state isn't valid, continue with next move
            }

            if (puzzleSolved) {
                break;
            }
        }
        if (!puzzleSolved && solution.size() > 0) {
            solution.pop();     // this last state didn't result in anything good, so pop it off the stack
        }
    }

    /**
     * Check if a given move was already played from a given puzzle state
     * @param state puzzle state to check
     * @param move move to check
     * @return whether this moved was already played (tried)
     */
    private boolean alreadyPlayed(PuzzleState state, Move move) {
        boolean found = false;
        for (Pair<PuzzleState, Move> pair : playedMoves) {
            if (pair.getLeft().equals(state) && pair.getRight().equals(move)) {
                found = true;
                break;
            }
        }

        return found;
    }

    /**
     * Get a list of available moves given the current state of the puzzle
     * @param state current puzzle state
     * @return list of available moves
     */
    public List<Move> getAvailableMoves(PuzzleState state) {
        // maximum possible combinations: 1C, 1M, 2C, 2M, 1C1M
        List<Move> availableMoves = new ArrayList<>();
        Move.Direction direction;
        List<Character> bank;

        if (state.getBoatPosition() == PuzzleState.Position.LEFT_BANK) {
            direction = Move.Direction.LEFT_TO_RIGHT;
            bank = state.getLeftBank();
        } else {
            direction = Move.Direction.RIGHT_TO_LEFT;
            bank = state.getRightBank();
        }

        // count cannibals
        long numCannibals = bank.stream().filter(p -> p == CANNIBAL).count();
        long numMissionaries = bank.stream().filter(p -> p == MISSIONARY).count();

        if (numCannibals >= 1) {
            availableMoves.add(new Move(direction, Arrays.asList(CANNIBAL)));
        }

        if (numCannibals >= 2) {
            availableMoves.add(new Move(direction, Arrays.asList(CANNIBAL, CANNIBAL)));
        }

        if (numMissionaries >= 1) {
            availableMoves.add(new Move(direction, Arrays.asList(MISSIONARY)));
        }

        if (numMissionaries >= 2) {
            availableMoves.add(new Move(direction, Arrays.asList(MISSIONARY, MISSIONARY)));
        }

        if (numCannibals >= 1 && numMissionaries >= 1) {
            availableMoves.add(new Move(direction, Arrays.asList(CANNIBAL, MISSIONARY)));
        }

        return availableMoves;
    }

    /**
     * Apply a move to a given puzzle state, producing a new state

     * @param state current puzzle state
     * @param move move to apply
     * @return new puzzle state
     */
    public PuzzleState apply(PuzzleState state, Move move) {
        // validate that the boat is on the right side
        PuzzleState.Position boatIsAt = state.getBoatPosition();
        if ((boatIsAt == PuzzleState.Position.LEFT_BANK && move.getDirection() == Move.Direction.RIGHT_TO_LEFT) ||
            (boatIsAt == PuzzleState.Position.RIGHT_BANK && move.getDirection() == Move.Direction.LEFT_TO_RIGHT)) {
            throw new PuzzleStateException("Move cannot be applied to current puzzle state!");
        }

        PuzzleState newState = new PuzzleState(state);  // copy existing state

        if (boatIsAt == PuzzleState.Position.LEFT_BANK) {
            newState.getLeftBank().remove(move.getPeople().get(0));
            newState.getRightBank().add(move.getPeople().get(0));
            if (move.getPeople().size() > 1) {
                newState.getLeftBank().remove(move.getPeople().get(1));
                newState.getRightBank().add(move.getPeople().get(1));
            }
            newState.setBoatPosition(PuzzleState.Position.RIGHT_BANK);
        } else {
            newState.getRightBank().remove(move.getPeople().get(0));
            newState.getLeftBank().add(move.getPeople().get(0));
            if (move.getPeople().size() > 1) {
                newState.getRightBank().remove(move.getPeople().get(1));
                newState.getLeftBank().add(move.getPeople().get(1));
            }
            newState.setBoatPosition(PuzzleState.Position.LEFT_BANK);
        }

        return newState;
    }
}
