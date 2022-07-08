package solutioning.strategy;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ExecutionSummary<S> {

    boolean successful;
    List<Action<S>> successSteps = null;
    Instant start;
    Instant end;

    public ExecutionSummary(boolean successful, List<Action<S>> successSteps, Instant start, Instant end){
        this.successful = successful;
        if(successful){
            this.successSteps = successSteps;
        }
        this.start = start;
        this.end = end;
    }

    public void print(){
        System.out.println("Success: " + successful);
        if(successful){
            System.out.println("Steps:");
            successSteps.forEach(action -> System.out.println(action.getName()));
        }
        System.out.println("Start: " + start.toString());
        System.out.println("End: " + end.toString());
        System.out.println("Duration: " + Duration.between(start, end).toMillis() + " milliseconds");
    }

    public boolean isSuccessful() {
        return successful;
    }

    public List<Action<S>> getSuccessSteps() {
        return successSteps;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
