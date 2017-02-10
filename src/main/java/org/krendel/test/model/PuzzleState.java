package org.krendel.test.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.krendel.test.exception.PuzzleStateException;

import java.util.ArrayList;
import java.util.List;

import static org.krendel.test.Solver.CANNIBAL;
import static org.krendel.test.Solver.MISSIONARY;

/**
 * This class represents a state of the puzzle, who's on the left bank, right bank, and where the boat is.
 */
public class PuzzleState {
    public enum Position {
        LEFT_BANK,
        RIGHT_BANK
    }

    private List<Character> leftBank;
    private List<Character> rightBank;
    private Position boatPosition;

    public PuzzleState(List<Character> leftBank, List<Character> rightBank, Position boatPosition) {
        this.leftBank = leftBank != null ? leftBank : new ArrayList<>();
        this.rightBank = rightBank != null ? rightBank : new ArrayList<>();
        this.boatPosition = boatPosition;
        validateState();
    }

    // copy constructor
    public PuzzleState(PuzzleState toCopy) {
        this.leftBank = toCopy.getLeftBank() == null ? null : new ArrayList<>(toCopy.getLeftBank());
        this.rightBank = toCopy.getRightBank() == null ? null : new ArrayList<>(toCopy.getRightBank());
        this.boatPosition = toCopy.boatPosition;
    }

    /**
     * Check if the current puzzle state is valid (cannibals are not outnumbering missionaries).
     */
    public boolean isValid() {
        validateState();    // if this fails there is a serious problem with applying or coming up with moves

        // more cannibals than missionaries on the left bank
        long leftMissionaries = count(getLeftBank(), MISSIONARY);
        long leftCannibals  = count(getLeftBank(), CANNIBAL);
        if (leftCannibals > leftMissionaries && leftMissionaries > 0) {
            return false;
        }

        // more cannibals than missionaries on the right bank
        long rightMissionaries = count(getRightBank(), MISSIONARY);
        long rightCannibals  = count(getRightBank(), CANNIBAL);
        if (rightCannibals > rightMissionaries && rightMissionaries > 0) {
            return false;
        }

        return true;
    }

    /**
     * Check the current puzzle state to determine if we've solved the problem.
     */
    public boolean isFinalState() {
        return (leftBank.size() == 0 && rightBank.size() == 6);
    }

    public List<Character> getLeftBank() {
        return leftBank;
    }

    public List<Character> getRightBank() {
        return rightBank;
    }

    public Position getBoatPosition() {
        return boatPosition;
    }

    public void setBoatPosition(Position position) {
        this.boatPosition = position;
    }

    /**
     * Count people of a given 'type' (missionaries or cannibals) in a given location (boat, etc.)
     */
    private long count(List<Character> location, Character type) {
        return location.stream().filter(c -> c == type).count();
    }

    /**
     * Verify that the current puzzle state is legal
     */
    private void validateState() {
        if (leftBank.size() > 6) {
            throw new PuzzleStateException("too many people on the left bank!");
        }

        if (rightBank.size() > 6) {
            throw new PuzzleStateException("too many people on the left bank!");
        }

        if (leftBank.size() + rightBank.size() > 6) {
            throw new PuzzleStateException("too many people on the board!");
        }

        // more than 3 missionaries
        if (count(leftBank, MISSIONARY) + count(rightBank, MISSIONARY) > 3) {
            throw new PuzzleStateException("too many missionaries on the board!");
        }

        // more than 3 cannibals
        if (count(leftBank, CANNIBAL) + count(rightBank, CANNIBAL) > 3) {
            throw new PuzzleStateException("too many cannibals on the board!");
        }

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(49, 11).
                append(leftBank).
                append(rightBank).
                append(boatPosition).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        PuzzleState rhs = (PuzzleState) obj;

        if (leftBank.stream().filter(p -> p == CANNIBAL).count() != rhs.leftBank.stream().filter(p -> p == CANNIBAL).count()) {
            return false;
        }

        if (rightBank.stream().filter(p -> p == CANNIBAL).count() != rhs.rightBank.stream().filter(p -> p == CANNIBAL).count()) {
            return false;
        }

        if (leftBank.stream().filter(p -> p == MISSIONARY).count() != rhs.leftBank.stream().filter(p -> p == MISSIONARY).count()) {
            return false;
        }

        if (rightBank.stream().filter(p -> p == MISSIONARY).count() != rhs.rightBank.stream().filter(p -> p == MISSIONARY).count()) {
            return false;
        }

        return (boatPosition == rhs.boatPosition);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("left bank", leftBank)
                .append("right bank", rightBank)
                .append("boat position", boatPosition)
                .toString();
    }
}
