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

    public ScoreResult(List<Pair<Action<S>, Double>> actionScoreList, Subject<S> subject){
        this.score = actionScoreList != null && actionScoreList.size() > 0 ? actionScoreList.get(actionScoreList.size() - 1).getValue1() : 0.0;
        this.actionScoreList = actionScoreList != null ? new ArrayList<>(actionScoreList) : new ArrayList<>();
        this.subject = subject;
    }

    public ScoreResult(Double score, Subject<S> subject){
        this.score = score;
        this.actionScoreList = new ArrayList<>();
        this.subject = subject;
    }

    public ScoreResult<S> empty(){
        return new ScoreResult<>(
                this.score,
                this.getSubject()
        );
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
        this.score = actionScoreList.stream().reduce(0.0,
                (acc, item) -> item.getValue1() > acc ? item.getValue1() : acc,
                (a, b) -> a > b ? a : b
                );
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
        return actionScoreList != null ? actionScoreList.size() : 0;
    }

    public Pair<Action<S>, Double> getLastActionScore(){
        if(actionScoreList == null || actionScoreList.size() == 0) return null;
        return actionScoreList.get(getActionCount() - 1);
    }

    @Override
    public int compareTo(ScoreResult o) {
        return Double.compare(o.score, score);
    }
}
