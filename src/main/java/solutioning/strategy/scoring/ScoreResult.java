package solutioning.strategy.scoring;

import org.javatuples.Pair;
import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.ArrayList;
import java.util.List;

public class ScoreResult<S> implements Comparable<ScoreResult<S>> {

    private Double score;
    private final List<Pair<Action<S>, Double>> actionScoreList;
    private final Subject<S> subject;

    public ScoreResult(Double score, List<Pair<Action<S>, Double>> actionScoreList, Subject<S> subject){
        this.score = score;
        this.actionScoreList = actionScoreList;
        this.subject = subject;
    }

    public static <S> ScoreResult<S> empty(Subject<S> subject, Double score) {
        return new ScoreResult<>(score, new ArrayList<>(), subject);
    }

    public Double getScore() {
        return score;
    }

    public List<Pair<Action<S>, Double>> getActionScoreList() {
        return actionScoreList;
    }

    public Subject<S> getSubject() {
        return subject;
    }

    public void addActionScore(Pair<Action<S>, Double> actionScore){
        this.actionScoreList.add(actionScore);
        this.score = actionScore.getValue1();
    }

    public Action<S> lastAction(){
        if(actionScoreList == null || actionScoreList.size() == 0) return null;
        return actionScoreList.get(actionScoreList.size() - 1).getValue0();
    }

    public List<Action<S>> getActionList(){
        return actionScoreList.stream().map(Pair::getValue0).toList();
    }

    public List<Double> getScoreList(){
        return actionScoreList.stream().map(Pair::getValue1).toList();
    }

    public int getActionCount(){
        return actionScoreList.size();
    }

    @Override
    public int compareTo(ScoreResult o) {
        return Double.compare(o.score, score);
    }
}
