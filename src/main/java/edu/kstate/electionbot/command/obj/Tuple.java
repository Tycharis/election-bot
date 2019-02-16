package edu.kstate.electionbot.command.obj;

public class Tuple<T1, T2> implements Comparable {
    private T1 left;
    private T2 right;

    private Tuple(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }

    T1 getLeft() {
        return left;
    }

    T2 getRight() {
        return right;
    }

    @Override
    public int compareTo(Object o) {
        return 0; //TODO implement sorting
    }
}