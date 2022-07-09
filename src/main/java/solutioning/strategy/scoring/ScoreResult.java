package solutioning.strategy.scoring;

import org.javatuples.Pair;
import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.ArrayList;
import java.util.List;

public record ScoreResult<S>(
        Double score,
        List<Pair<Action<S>, Double>> actionScoreList,
        Subject<S> subject) implements Comparable<ScoreResult<S>> {

    public static <S> ScoreResult<S> empty(Subject<S> subject) {
        return new ScoreResult<>(0.0, new ArrayList<>(), subject);
    }

    public Action<S> lastAction(){
        if(actionScoreList == null || actionScoreList.size() == 0) return null;
        return actionScoreList.get(actionScoreList.size() - 1).getValue0();
    }

    public List<Action<S>> getActionList(){
        return actionScoreList().stream().map(Pair::getValue0).toList();
    }

    public List<Double> getScoreList(){
        return actionScoreList().stream().map(Pair::getValue1).toList();
    }

    @Override
    public int compareTo(ScoreResult o) {
        return Double.compare(o.score, score);
    }
}
