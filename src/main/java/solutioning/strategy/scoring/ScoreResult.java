package solutioning.strategy.scoring;

import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.ArrayList;
import java.util.List;

public record ScoreResult<S>(
        Double score,
        List<Action<S>> actionList,
        Subject<S> subject) implements Comparable<ScoreResult<S>> {

    public static <S> ScoreResult<S> empty(Subject<S> subject) {
        return new ScoreResult<>(0.0, new ArrayList<>(), subject);
    }

    public Action<S> lastAction(){
        if(actionList == null || actionList.size() == 0) return null;
        return actionList.get(actionList.size() - 1);
    }

    @Override
    public int compareTo(ScoreResult o) {
        return Double.compare(o.score, score);
    }
}
