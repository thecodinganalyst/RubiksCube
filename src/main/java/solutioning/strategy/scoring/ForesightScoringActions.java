package solutioning.strategy.scoring;

import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.ArrayList;
import java.util.List;

public class ForesightScoringActions<S> {

    private final ScoringMechanism<S> scoringMechanism;

    public ForesightScoringActions(ScoringMechanism<S> scoringMechanism){
        this.scoringMechanism = scoringMechanism;
    }

    public List<ScoreResult<S>> getRankedResultsForAllPossibleActions(ScoreResult<S> result) {

        List<ScoreResult<S>> candidates = new ArrayList<>();

        Action<S> lastAction = result.lastAction();

        for(Action<S> action: result.subject().getAllActions()){
            try{
                if(lastAction != null && action.equals(lastAction.oppositeAction())) continue;
                Subject<S> clone = result.subject().clone();
                clone.performAction(action);
                double score = scoringMechanism.getScore(clone);
                List<Action<S>> updatedList = new ArrayList<>(result.actionList());
                updatedList.add(action);
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
                .filter(scoreResult -> !scoresToRemove.contains(scoreResult.score()))
                .toList();
    }

    public List<ScoreResult<S>> getBestScore(List<ScoreResult<S>> scoreResultList, int bestScoreCount){
        if(scoreResultList.size() > bestScoreCount){
            return scoreResultList.subList(0, bestScoreCount);
        }
        return scoreResultList;
    }

    public List<Double> getLastFewScoresToSkip(List<ScoreResult<S>> scoreResultList, int skipLastScoreCount){
        List<Double> scoreList = scoreResultList.stream().map(ScoreResult::score).toList();
        if(scoreList.size() <= skipLastScoreCount) return scoreList;
        return scoreList.subList(scoreList.size() - skipLastScoreCount, scoreList.size());
    }
}
