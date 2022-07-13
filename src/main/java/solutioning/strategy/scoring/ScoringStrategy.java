package solutioning.strategy.scoring;

import solutioning.strategy.Action;
import solutioning.strategy.ExecutionSummary;
import solutioning.strategy.Strategy;
import solutioning.strategy.Subject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ScoringStrategy<S> implements Strategy<S> {
    int limit;
    ScoringMechanism<S> scoringMechanism;

    public ScoringStrategy(int limit, ScoringMechanism<S> scoringMechanism){
        this.limit = limit;
        this.scoringMechanism = scoringMechanism;
    }

    public Action<S> getActionWithHighestScore(Subject<S> subject) throws CloneNotSupportedException {
        double highestScore = 0;
        Action<S> bestAction = null;
        for(Action<S> action: subject.getAllActions()){
            subject.clone().performAction(action);
            double score = scoringMechanism.getScore(subject);
            if(score > highestScore){
                highestScore = score;
                bestAction = action;
            }
            subject.performAction(action.oppositeAction());
        }
        return bestAction;
    }

    public ExecutionSummary<S> performBestScoreTrial(Subject<S> subject) throws CloneNotSupportedException {
        Instant start = Instant.now();
        List<Action<S>> actionList = new ArrayList<>();
        boolean found = false;
        for(int i = 0; i < limit; i++){
            Action<S> bestAction = getActionWithHighestScore(subject);
            subject.performAction(bestAction);
            System.out.println(i + ": " + bestAction.getName());
            subject.print();
            System.out.println(scoringMechanism.getScore(subject));
            actionList.add(bestAction);
            if(subject.isComplete()){
                found = true;
                break;
            }
        }
        Instant end = Instant.now();
        return new ExecutionSummary<>(found, found ? actionList : null, start, end);
    }

    @Override
    public ExecutionSummary<S> execute(Subject<S> subject) {
        ExecutionSummary<S> executionSummary = null;
        try{
            executionSummary = performBestScoreTrial(subject);
        } catch (Exception e){
            e.printStackTrace();
        }
        return executionSummary;
    }
}
