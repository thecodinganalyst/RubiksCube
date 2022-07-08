package rubikcube.strategy;

import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;
import rubikcube.action.ConsolidatedAction;
import rubikcube.scoring.RubikCubeScoringByCompletionPercentage;
import solutioning.strategy.Action;
import solutioning.strategy.scoring.ForesightScoringActions;
import solutioning.strategy.scoring.ScoreResult;
import solutioning.strategy.scoring.ScoringMechanism;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

class ForesightScoringActionsTest {

    ScoringMechanism<RubikCube> rubikCubeScoring = new RubikCubeScoringByCompletionPercentage();
    ForesightScoringActions<RubikCube> foresightScoringActions = new ForesightScoringActions<>(rubikCubeScoring);

    @Test
    void getRankedResultsForAllPossibleActions_WhenScoreResultIsNotEmpty_ShouldOmitPreviousAction() {
        RubikCube rubikCube = new RubikCube(3);
        Action<RubikCube> action1 = new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3);
        Action<RubikCube> action2 = new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 1, 3);
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                1.2,
                List.of(action1, action2),
                rubikCube);
        List<ScoreResult<RubikCube>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(scoreResult);

        assertThat(scoreResultList.size(), equalTo(rubikCube.getAllActions().length - 1));

        Action<RubikCube> oppLastAction = action2.oppositeAction();
        Double prevScore = scoreResultList.get(0).score();
        for (int i = 1; i < scoreResultList.size(); i++) {
            ScoreResult<RubikCube> result = scoreResultList.get(i);
            Double currentScore = result.score();
            assertThat(currentScore, lessThanOrEqualTo(prevScore));
            assertThat(oppLastAction, not(equalTo(scoreResult.lastAction())));
            assertThat(result.actionList().size(), equalTo(3));
            prevScore = currentScore;
        }
    }

    @Test
    void getRankedResultsForAllPossibleActions_WhenScoreResultIsEmpty_ShouldHaveSameNumberOfResultsAsNumberOfActions() {
        RubikCube rubikCube = new RubikCube(3);
        List<ScoreResult<RubikCube>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(ScoreResult.empty(rubikCube));

        assertThat(scoreResultList.size(), equalTo(rubikCube.getAllActions().length));
        for(ScoreResult<RubikCube> result: scoreResultList){
            assertThat(result.actionList().size(), equalTo(1));
        }
        Double prevScore = scoreResultList.get(0).score();
        for(int i = 1; i < scoreResultList.size(); i++){
            Double currentScore = scoreResultList.get(i).score();
            assertThat(currentScore, lessThanOrEqualTo(prevScore));
            prevScore = currentScore;
        }
    }

    @Test
    void filterScoreResultListByRemovingItemsWithCertainScores(){
        RubikCube rubikCube = new RubikCube(3);
        List<ScoreResult<RubikCube>> scoreResultList = List.of(
                new ScoreResult<>(0.422, null, rubikCube),
                new ScoreResult<>(0.323, null, rubikCube),
                new ScoreResult<>(0.234, null, rubikCube),
                new ScoreResult<>(0.222, null, rubikCube),
                new ScoreResult<>(0.222, null, rubikCube),
                new ScoreResult<>(0.121, null, rubikCube),
                new ScoreResult<>(0.121, null, rubikCube),
                new ScoreResult<>(0.023, null, rubikCube),
                new ScoreResult<>(0.010, null, rubikCube),
                new ScoreResult<>(0.001, null, rubikCube)
        );
        List<Double> scores = List.of(0.222, 0.023, 0.010);
        List<ScoreResult<RubikCube>> result = foresightScoringActions.filterScoreResultListByRemovingItemsWithCertainScores(scoreResultList, scores);
        assertThat(result.size(), equalTo(6));
        for (ScoreResult<RubikCube> scoreResult: result){
            assertThat(scoreResult.score(), not(equalTo(0.222)));
            assertThat(scoreResult.score(), not(equalTo(0.023)));
            assertThat(scoreResult.score(), not(equalTo(0.010)));
        }
    }

    @Test
    void getBestScore_shouldReturnCorrectResults() {
        RubikCube rubikCube = new RubikCube(3);
        List<ScoreResult<RubikCube>> scoreResultList = List.of(
                new ScoreResult<>(0.422, null, rubikCube),
                new ScoreResult<>(0.323, null, rubikCube),
                new ScoreResult<>(0.234, null, rubikCube),
                new ScoreResult<>(0.222, null, rubikCube),
                new ScoreResult<>(0.222, null, rubikCube),
                new ScoreResult<>(0.121, null, rubikCube),
                new ScoreResult<>(0.121, null, rubikCube),
                new ScoreResult<>(0.023, null, rubikCube),
                new ScoreResult<>(0.010, null, rubikCube),
                new ScoreResult<>(0.001, null, rubikCube)
        );
        List<ScoreResult<RubikCube>> results = foresightScoringActions.getBestScore(scoreResultList, 5);
        assertThat(results.size(), equalTo(5));
        assertThat(results.get(0).score(), equalTo(0.422));
        assertThat(results.get(1).score(), equalTo(0.323));
        assertThat(results.get(2).score(), equalTo(0.234));
        assertThat(results.get(3).score(), equalTo(0.222));
        assertThat(results.get(4).score(), equalTo(0.222));
    }

    @Test
    void getBestScore_whenBestScoreCountIsLessThanListSize_shouldReturnAllResults() {
        RubikCube rubikCube = new RubikCube(3);
        List<ScoreResult<RubikCube>> scoreResultList = List.of(
                new ScoreResult<>(0.422, null, rubikCube),
                new ScoreResult<>(0.323, null, rubikCube),
                new ScoreResult<>(0.234, null, rubikCube)
        );
        List<ScoreResult<RubikCube>> results = foresightScoringActions.getBestScore(scoreResultList, 5);
        assertThat(results.size(), equalTo(3));
        assertThat(results.get(0).score(), equalTo(0.422));
        assertThat(results.get(1).score(), equalTo(0.323));
        assertThat(results.get(2).score(), equalTo(0.234));
    }

    @Test
    void getLastFewScoresToSkip_shouldGetLastNScores() {
        List<ScoreResult<RubikCube>> scoreResultList = List.of(
                new ScoreResult<>(0.345, null, null),
                new ScoreResult<>(0.234, null, null),
                new ScoreResult<>(0.222, null, null),
                new ScoreResult<>(0.221, null, null),
                new ScoreResult<>(0.199, null, null),
                new ScoreResult<>(0.188, null, null),
                new ScoreResult<>(0.177, null, null),
                new ScoreResult<>(0.176, null, null)
        );
        List<Double> scoreList = foresightScoringActions.getLastFewScoresToSkip(scoreResultList, 5);
        assertThat(scoreList.size(), equalTo(5));
        assertThat(scoreList.get(0), equalTo(0.221));
        assertThat(scoreList.get(1), equalTo(0.199));
        assertThat(scoreList.get(2), equalTo(0.188));
        assertThat(scoreList.get(3), equalTo(0.177));
        assertThat(scoreList.get(4), equalTo(0.176));
    }

    @Test
    void getLastFewScoresToSkip_whenSkipCountIsLessThanListSize_shouldGetScoreOfWholeList() {
        List<ScoreResult<RubikCube>> scoreResultList = List.of(
                new ScoreResult<>(0.345, null, null),
                new ScoreResult<>(0.234, null, null),
                new ScoreResult<>(0.222, null, null)
        );
        List<Double> scoreList = foresightScoringActions.getLastFewScoresToSkip(scoreResultList, 5);
        assertThat(scoreList.size(), equalTo(3));
        assertThat(scoreList.get(0), equalTo(0.345));
        assertThat(scoreList.get(1), equalTo(0.234));
        assertThat(scoreList.get(2), equalTo(0.222));
    }

    @Test
    void getLastFewScoresToSkip_whenListIsEmpty_shouldReturnEmptyList() {
        List<ScoreResult<RubikCube>> scoreResultList = List.of();
        List<Double> scoreList = foresightScoringActions.getLastFewScoresToSkip(scoreResultList, 5);
        assertThat(scoreList.size(), equalTo(0));
    }
}