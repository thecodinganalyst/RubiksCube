package solutioning.strategy;

public interface Action<S> {
    String getName();
    void performAction(Subject<S> subject);
    Action<S> oppositeAction();
}
