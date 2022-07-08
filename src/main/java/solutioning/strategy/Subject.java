package solutioning.strategy;

import java.util.List;

public interface Subject<S> extends Cloneable{
    void performAction(Action<S> action);
    void performActionList(List<Action<S>> actionList);
    Action<S>[] getAllActions();
    boolean check();
    boolean isComplete();
    void print();
    Subject<S> clone() throws CloneNotSupportedException;
}
