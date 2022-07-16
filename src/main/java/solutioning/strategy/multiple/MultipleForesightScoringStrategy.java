package solutioning.strategy.multiple;

import solutioning.strategy.ExecutionSummary;
import solutioning.strategy.Strategy;
import solutioning.strategy.Subject;
import solutioning.strategy.scoring.ForesightScoringStrategy;
import solutioning.strategy.scoring.ScoringMechanism;

import java.time.Instant;

public class MultipleForesightScoringStrategy<S> implements Strategy<S> {

    private final int limit;
    private final ScoringMechanism<S> scoringMechanism;
    private int startForesightCount = ForesightScoringStrategy.DEFAULT_FORESIGHT_COUNT;
    private int foresightCountLimit = 12;

    public MultipleForesightScoringStrategy(int limit, ScoringMechanism<S> scoringMechanism){
        this.limit = limit;
        this.scoringMechanism = scoringMechanism;
    }

    @Override
    public ExecutionSummary<S> execute(Subject<S> subject) {

        ForesightScoringStrategy<S> foresightScoringStrategy = new ForesightScoringStrategy<>(limit, scoringMechanism);
        foresightScoringStrategy.setForesightCount(startForesightCount);
        ExecutionSummary<S> executionSummary = new ExecutionSummary<>(false, null, Instant.now(), Instant.now());

        executionSummary = executeIncrementalForesightCount(foresightScoringStrategy, subject);
        if(executionSummary.isSuccessful()){
            return executionSummary;
        }

        return executionSummary;
    }

    private ExecutionSummary<S> executeIncrementalForesightCount(ForesightScoringStrategy<S> foresightScoringStrategy, Subject<S> subject){
        ExecutionSummary<S> executionSummary = new ExecutionSummary<>(false, null, Instant.now(), Instant.now());
        try{
            while(foresightScoringStrategy.getForesightCount() <= foresightCountLimit ){
                executionSummary = foresightScoringStrategy.execute(subject.clone());
                if(executionSummary.isSuccessful()){
                    return executionSummary;
                }

                int newForesightCount = foresightScoringStrategy.incrementForesightCount();
                foresightScoringStrategy.incrementMaxForesightCount();

                System.out.println("Increment foresight count = " + newForesightCount);
            }
        }catch (CloneNotSupportedException ex){
            ex.printStackTrace();
        }
        return executionSummary;
    }
}
