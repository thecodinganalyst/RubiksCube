package solutioning.strategy.scoring;

import solutioning.strategy.Subject;

public interface ScoringMechanism<S> {
    Double getScore(Subject<S> subject);
}
