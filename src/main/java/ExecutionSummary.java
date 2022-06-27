import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ExecutionSummary {
    boolean successful;
    List<RubikCubeAction> successSteps = null;
    Instant start;
    Instant end;

    public ExecutionSummary(boolean successful, List<RubikCubeAction> successSteps, Instant start, Instant end){
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
}
