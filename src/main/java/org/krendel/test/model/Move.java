package org.krendel.test.model;

import org.krendel.test.exception.PuzzleStateException;

import java.util.List;

/**
 * This class represents a possible "move" in the puzzle
 */
public class Move {
    public enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    Direction direction;
    List<Character> people;

    public Move(Direction dir, List<Character> people) {
        this.direction = dir;
        this.people = people;
        if (people == null || people.size() > 2) {
            throw new PuzzleStateException("invalid Move created.");
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Character> getPeople() {
        return people;
    }

    @Override
    public int hashCode() {
        return 10 * direction.hashCode() + 20 * people.size() + 30 * people.hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Move)) {
            return false;
        }

        Move move = (Move) a;

        if (direction != move.direction) {
            return false;
        }

        if (people.size() != move.people.size()) {
            return false;
        }

        if (people.get(0) != move.people.get(0)) {
            return false;
        }

        if (people.size() > 1) {
            return (people.get(1) == move.people.get(1));
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format("%s [%s%s]", getDirection() == Move.Direction.LEFT_TO_RIGHT ? "--->" : "<---",
                getPeople().get(0), getPeople().size() > 1 ? getPeople().get(1) : "");

    }
}
