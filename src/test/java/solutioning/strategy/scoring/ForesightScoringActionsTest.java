package solutioning.strategy.scoring;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rubikcube.RubikCube;
import rubikcube.action.ConsolidatedAction;
import rubikcube.scoring.RubikCubeScoringByCompletionPercentage;
import solutioning.strategy.Action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ForesightScoringActionsTest {

    ScoringMechanism<RubikCube> rubikCubeScoring = new RubikCubeScoringByCompletionPercentage();
    ForesightScoringActions<RubikCube> foresightScoringActions = new ForesightScoringActions<>(rubikCubeScoring);

    @Test
    void getRankedResultsForAllPossibleActions_WhenScoreResultIsNotEmpty_ShouldOmitPreviousAction() {
        RubikCube rubikCube = new RubikCube(3);
        Action<RubikCube> action1 = new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3);
        Action<RubikCube> action2 = new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 1, 3);
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                List.of(Pair.with(action1, 0.1), Pair.with(action2, 1.2)),
                rubikCube);
        List<ScoreResult<RubikCube>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(scoreResult);

        assertThat(scoreResultList.size(), equalTo(rubikCube.getAllActions().length - 1));

        Action<RubikCube> oppLastAction = action2.oppositeAction();
        Double prevScore = scoreResultList.get(0).getScore();
        for (int i = 1; i < scoreResultList.size(); i++) {
            ScoreResult<RubikCube> result = scoreResultList.get(i);
            Double currentScore = result.getScore();
            assertThat(currentScore, lessThanOrEqualTo(prevScore));
            assertThat(oppLastAction, not(equalTo(scoreResult.lastAction())));
            assertThat(result.getActionScoreList().size(), equalTo(3));
            prevScore = currentScore;
        }
    }

    @Test
    void getRankedResultsForAllPossibleActions_WhenScoreResultIsEmpty_ShouldHaveSameNumberOfResultsAsNumberOfActions() {
        RubikCube rubikCube = new RubikCube(3);
        List<ScoreResult<RubikCube>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(new ScoreResult<>(1.0, rubikCube));

        assertThat(scoreResultList.size(), equalTo(rubikCube.getAllActions().length));
        for(ScoreResult<RubikCube> result: scoreResultList){
            assertThat(result.getActionScoreList().size(), equalTo(1));
        }
        Double prevScore = scoreResultList.get(0).getScore();
        for(int i = 1; i < scoreResultList.size(); i++){
            Double currentScore = scoreResultList.get(i).getScore();
            assertThat(currentScore, lessThanOrEqualTo(prevScore));
            prevScore = currentScore;
        }
    }

    @Test
    void getRankedResultsForAllPossibleActions_ShouldHaveUpdatedScoreForAllResults() {
        RubikCube rubikCube = new RubikCube(3);
        Action<RubikCube> action1 = new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3);
        Action<RubikCube> action2 = new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 1, 3);
        rubikCube.performAction(action1);
        rubikCube.performAction(action2);
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                List.of(Pair.with(action1, 0.1), Pair.with(action2, 1.2)),
                rubikCube);
        List<ScoreResult<RubikCube>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(scoreResult);

        assertThat(scoreResultList.size(), greaterThan(0));
        Double prevScore = scoreResultList.get(0).getScore();
        Set<String> actionSet = new HashSet<>();
        for (ScoreResult<RubikCube> rubikCubeScoreResult : scoreResultList) {
            assertThat(rubikCubeScoreResult.getActionCount(), equalTo(3));
            assertThat(rubikCubeScoreResult.getActionScoreList().get(0).getValue1(), equalTo(0.1));
            assertThat(rubikCubeScoreResult.getActionScoreList().get(1).getValue1(), equalTo(1.2));
            Double newScore = rubikCubeScoreResult.getScore();
            Double lastScore = rubikCubeScoreResult.getActionScoreList().get(2).getValue1();
            assertThat(newScore, equalTo(lastScore));
            assertThat(prevScore, greaterThanOrEqualTo(lastScore));
            prevScore = lastScore;
            String lastAction = rubikCubeScoreResult.lastAction().getName();
            assertThat(actionSet.add(lastAction), equalTo(true));
        }
        Action<RubikCube> action2opp = action2.oppositeAction();
        assertThat(actionSet.contains(action2opp.getName()), equalTo(false));
        assertThat(actionSet.size(), equalTo(17));
    }

    @Test
    void filterScoreResultListByRemovingItemsWithCertainScores(){
        RubikCube rubikCube = new RubikCube(3);
        List<ScoreResult<RubikCube>> scoreResultList = List.of(
                new ScoreResult<>(0.422, rubikCube),
                new ScoreResult<>(0.323, rubikCube),
                new ScoreResult<>(0.234, rubikCube),
                new ScoreResult<>(0.222, rubikCube),
                new ScoreResult<>(0.222, rubikCube),
                new ScoreResult<>(0.121, rubikCube),
                new ScoreResult<>(0.121, rubikCube),
                new ScoreResult<>(0.023, rubikCube),
                new ScoreResult<>(0.010, rubikCube),
                new ScoreResult<>(0.001, rubikCube)
        );
        List<Double> scores = List.of(0.222, 0.023, 0.010);
        List<ScoreResult<RubikCube>> result = foresightScoringActions.filterScoreResultListByRemovingItemsWithCertainScores(scoreResultList, scores);
        assertThat(result.size(), equalTo(6));
        for (ScoreResult<RubikCube> scoreResult: result){
            assertThat(scoreResult.getScore(), not(equalTo(0.222)));
            assertThat(scoreResult.getScore(), not(equalTo(0.023)));
            assertThat(scoreResult.getScore(), not(equalTo(0.010)));
        }
    }

    @Test
    void getLastFewScoresToSkip_shouldGetLastNScoresOnlyIfTheyAreDuplicates() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                List.of(
                        Pair.with(null, 0.176),
                        Pair.with(null, 0.177),
                        Pair.with(null, 0.188),
                        Pair.with(null, 0.199),
                        Pair.with(null, 0.221),
                        Pair.with(null, 0.222),
                        Pair.with(null, 0.222),
                        Pair.with(null, 0.345)
                ),
                null);

        List<Double> scoreList = foresightScoringActions.getLastFewScoresToSkip(scoreResult, 5);
        assertThat(scoreList.size(), equalTo(1));
        assertThat(scoreList.get(0), equalTo(0.222));
    }

    @Test
    void getLastFewScoresToSkip_whenSkipCountIsLessThanListSize_AndNoDuplicates_shouldGetEmptyList() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                List.of(
                        Pair.with(null, 0.176),
                        Pair.with(null, 0.177),
                        Pair.with(null, 0.188)
                ),
                null);
        List<Double> scoreList = foresightScoringActions.getLastFewScoresToSkip(scoreResult, 5);
        assertThat(scoreList.size(), equalTo(0));
    }

    @Test
    void getLastFewScoresToSkip_whenListIsEmpty_shouldReturnEmptyList() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                0.345,
                null);

        List<Double> scoreList = foresightScoringActions.getLastFewScoresToSkip(scoreResult, 5);
        assertThat(scoreList.size(), equalTo(0));
    }

    @Test
    void processEnough_whenScoreResultListIsEmpty_shouldReturnFalse() {
        boolean processEnough = foresightScoringActions.processEnough(null,  2, 4, 0.5);
        assertThat(processEnough, equalTo(false));

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(false));

        RubikCube rubikCube = new RubikCube(3);
        scoreResultList.add(new ScoreResult<>(1.0, rubikCube));
        processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(false));
    }

    @Test
    void processEnough_whenActionCountIsLessThanForesightCount_shouldReturnFalse() {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.2)), rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(false));

    }

    @Test
    void processEnough_whenActionCountIsEqualToForesightCountAndBetterThanPrevScore_shouldReturnTrue() {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.2), Pair.with(action, 0.51)), rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(true));
    }

    @Test
    void processEnough_whenActionCountIsEqualToForesightCountAndEqualWithPrevScore_shouldReturnFalse() {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.2), Pair.with(action, 0.5)), rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(false));
    }

    @Test
    void processEnough_whenActionCountIsEqualToForesightCountAndLowerThanPrevScore_shouldReturnFalse() {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.2), Pair.with(action, 0.45)), rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(false));
    }

    @Test
    void processEnough_whenActionCountIsHigherThanForesightCountLowerThanMaxHigherThanPrevScore_ShouldReturnTrue() {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.2), Pair.with(action, 0.3), Pair.with(action, 0.6)), rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(true));
    }

    @Test
    void processEnough_whenActionCountIsHigherThanForesightCountLowerThanMaxLowerThanPrevScore_ShouldReturnFalse() {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.2), Pair.with(action, 0.3), Pair.with(action, 0.38)), rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList,2, 4, 0.5);
        assertThat(processEnough, equalTo(false));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.3, 0.4, 0.45, 0.5, 0.6})
    void processEnough_whenActionCountIsEqualToMax_ShouldReturnFalse(double lastScore) {
        RubikCube rubikCube = new RubikCube(3);

        List<ScoreResult<RubikCube>> scoreResultList = new ArrayList<>();
        ConsolidatedAction action = Mockito.mock(ConsolidatedAction.class);
        ScoreResult<RubikCube> bestScoreResult = new ScoreResult<>(List.of(
                Pair.with(action, 0.2),
                Pair.with(action, 0.3),
                Pair.with(action, 0.38),
                Pair.with(action, lastScore)),
        rubikCube);
        scoreResultList.add(0, bestScoreResult);
        boolean processEnough = foresightScoringActions.processEnough(scoreResultList, 2, 4, 0.5);
        assertThat(processEnough, equalTo(true));
    }
}