package solutioning.strategy.random;

import solutioning.strategy.Action;
import solutioning.strategy.ExecutionSummary;
import solutioning.strategy.Strategy;
import solutioning.strategy.Subject;

import java.time.Instant;
import java.util.*;

public class RandomActionStrategy<S> implements Strategy<S> {

    Random random = new Random();
    int limit;
    int maxSteps;
    int successCount;

    public RandomActionStrategy(int limit, int maxSteps, int successCount){
        this.limit = limit;
        this.maxSteps = maxSteps;
        this.successCount = successCount;
    }

    @Override
    public ExecutionSummary<S> execute(Subject<S> subject){
        Instant start = Instant.now();
        int success = 0;
        Collection<ExecutionSummary<S>> successList = new ArrayList<>();

        for(int i = 0; i < limit; i++){
            try{
                Subject<S> clone = subject.clone();
                ExecutionSummary<S> executionSummary = performRandomActionsUntilComplete(subject, maxSteps);

                if(executionSummary.isSuccessful()){
                    success++;
                    successList.add(executionSummary);
                    System.out.println(i + ": " + clone.check());
                    clone.print();
                    if(success == successCount) break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ExecutionSummary<S> fastest = successList.stream()
                                                .min(Comparator.comparingInt(a -> a.getSuccessSteps().size()))
                                                .orElse(null);

        Instant end = Instant.now();

        return new ExecutionSummary<>(
                fastest != null,
                fastest != null ? fastest.getSuccessSteps() : null,
                start,
                end);
    }

    public ExecutionSummary<S> performRandomActionsUntilComplete(Subject<S> subject, int withinSteps){
        Instant start = Instant.now();

        List<Action<S>> actions = new ArrayList<>();
        for (int i = 0; i < withinSteps; i ++){
            Action<S> action = performRandomAction(subject);
            actions.add(action);
            System.out.println(i + ": " + action.getName());
            subject.print();
            if(subject.isComplete()) break;
        }

        Instant end = Instant.now();
        return new ExecutionSummary<>(subject.isComplete(), actions, start, end);
    }

    private Action<S> getRandomAction(Action<S>[] allPossibleActions){
        int rand = random.nextInt(allPossibleActions.length);
        return allPossibleActions[rand];
    }

    public Action<S> performRandomAction(Subject<S> subject){
        Action<S> action = getRandomAction(subject.getAllActions());
        action.performAction(subject);
        return action;
    }
}
