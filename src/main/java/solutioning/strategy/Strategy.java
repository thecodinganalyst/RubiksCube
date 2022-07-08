package solutioning.strategy;

public interface Strategy<S> {
    ExecutionSummary<S> execute(Subject<S> subject);
}
