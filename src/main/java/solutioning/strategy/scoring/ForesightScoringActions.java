package solutioning.strategy.scoring;

import org.javatuples.Pair;
import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.*;

public class ForesightScoringActions<S> {

    private final ScoringMechanism<S> scoringMechanism;

    public ForesightScoringActions(ScoringMechanism<S> scoringMechanism){
        this.scoringMechanism = scoringMechanism;
    }

    public List<ScoreResult<S>> getRankedResultsForAllPossibleActions(ScoreResult<S> result) {

        List<ScoreResult<S>> candidates = new ArrayList<>();

        Action<S> lastAction = result.lastAction();

        for(Action<S> action: result.getSubject().getAllActions()){
            try{
                if(lastAction != null && action.equals(lastAction.oppositeAction())) continue;
                Subject<S> clone = result.getSubject().clone();
                clone.performAction(action);
                double score = scoringMechanism.getScore(clone);
                List<Pair<Action<S>, Double>> updatedList = new ArrayList<>(result.getActionScoreList());
                updatedList.add(Pair.with(action, score));
                if(clone.isComplete()) return List.of(new ScoreResult<>(score, updatedList, clone));
                candidates.add(new ScoreResult<>(score, updatedList, clone));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return candidates.stream().sorted().toList();
    }

    public List<ScoreResult<S>> filterScoreResultListByRemovingItemsWithCertainScores(List<ScoreResult<S>> scoreResultList, List<Double> scoresToRemove){
        return scoreResultList.stream()
                .filter(scoreResult -> !scoresToRemove.contains(scoreResult.getScore()))
                .toList();
    }

    public List<ScoreResult<S>> getBestScore(List<ScoreResult<S>> scoreResultList, int bestScoreCount){
        if(scoreResultList.size() > bestScoreCount){
            return scoreResultList.subList(0, bestScoreCount);
        }
        return scoreResultList;
    }

    public List<Double> getLastFewScoresToSkip(ScoreResult<S> scoreResult, int skipLastScoreCount){
        List<Double> scoreList = scoreResult.getScoreList();
        List<Double> subList = scoreList;
        if(skipLastScoreCount < subList.size()){
            subList = scoreList.subList(scoreList.size() - skipLastScoreCount, scoreList.size());
        }
        Set<Double> pastScores = new HashSet<>();
        return subList.stream().filter(i -> !pastScores.add(i)).toList();
    }
}