/*
 * ModelCompetition.java
 * Acts as a viewmodel/controller to the frame FrameCompetition.java.
 * Manages the top level API.
 */
package swimmingcompetition.ux.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import swimmingcompetition.persistence.Persistence;
import swimmingcompetition.simulator.Color;
import swimmingcompetition.simulator.CompetitionState;
import swimmingcompetition.simulator.Gender;
import swimmingcompetition.simulator.Judge;
import swimmingcompetition.simulator.Spectator;
import swimmingcompetition.simulator.Stroke;
import swimmingcompetition.simulator.SupportingStaffMember;
import swimmingcompetition.simulator.Swimmer;
import swimmingcompetition.simulator.SwimmingCompetition;

/**
 *
 * @author Wickramaranga
 */
public class ModelCompetition {

    private final SwimmingCompetition competition;

    private final int initialX; // Initial position of swimmers in the GUI. 
    private final int finalX; // Final position of swimmers in the GUI.
    private final double lengthFactor; // Ratio between swimming lane and the GUI lane in pixels. 
    private final int refreshFactor; // Determines the millisecond wait for pulling data.

    private String message;
    private UIUpdater generalUpdater;
    private UIUpdater poolUpdater;
    private UIUpdater scoreboardUpdater;

    private Timer pullTimer;

    public ModelCompetition(SwimmingCompetition competition) {
        this.competition = competition;
        this.initialX = 10;
        this.lengthFactor = 1.0 * 760 / competition.getPoolLength(); // double
        this.finalX = 760;
        this.refreshFactor = 70;
        // TODO Calculate precise timing
        // Sync with the sleep value in swim lane to make it pixel perfect.  
        // Infer-pull hybrid
        message = "Click 'New Round' to get started. ";
    }

    /**
     * Sets the updater for updating general UI components.
     *
     * @param updater the generalUpdater to set.
     */
    public void setGeneralUpdater(UIUpdater updater) {
        this.generalUpdater = updater;
    }

    /**
     * Sets the updater for updating swimming pool.
     *
     * @param poolUpdater the poolUpdater to set.
     */
    public void setPoolUpdater(UIUpdater poolUpdater) {
        this.poolUpdater = poolUpdater;
    }

    /**
     * Sets the updater for updating scoreboard.
     *
     * @param scoreboardUpdater the scoreboardUpdater to set.
     */
    public void setScoreboardUpdater(UIUpdater scoreboardUpdater) {
        this.scoreboardUpdater = scoreboardUpdater;
    }

    /**
     * Gets the informative message.
     *
     * @return gets the message.
     */
    public String getMessage() {
        return message;
    }

    // What do we need to show in the beginning when the competition is created?
    /**
     * Gets the total lane count.
     *
     * @return lane count.
     */
    public int getLaneCount() {
        return competition.getLaneCount();
    }

    /**
     * Gets a list of all swimmers.
     *
     * @return get all swimmers.
     */
    public List<Swimmer> getSwimmers() {
        return competition.getSwimmers();
    }

    /**
     * Gets a list of all spectators.
     *
     * @return get all spectators.
     */
    public List<String> getSpectatorNames() {
        List<String> names = new ArrayList<>();
        for (Spectator s : competition.getSpectators()) {
            names.add(s.getName());
        }
        return names;
    }

    /**
     * Gets a list of all judges.
     *
     * @return get all judge names.
     */
    public List<String> getJudgeNames() {
        List<String> names = new ArrayList<>();
        for (Judge j : competition.getJudges()) {
            names.add(j.getName());
        }
        return names;
    }

    /**
     * Gets a list of all staff member names.
     *
     * @return get all staff member names.
     */
    public List<String> getStaffMemberNames() {
        List<String> names = new ArrayList<>();
        for (SupportingStaffMember s : competition.getStaff()) {
            names.add(s.getName());
        }
        return names;
    }

    // What do we need when the competition is ready?
    /**
     * Gets the initial GUI position of swimmers.
     *
     * @return the initialX.
     */
    public int getInitialX() {
        return initialX;
    }

    /**
     * Gets the number of active swimmers.
     *
     * @return active swimmer count.
     */
    public int getActiveLaneCount() {
        // One lane per each active swimmer. 
        return competition.getActiveSwimmers().size();
    }

    /**
     * Gets the uniform color for the round.
     *
     * @return the uniform color for the round.
     */
    public Color getUniformColor() {
        // Fetch color from the first active swimmer. 
        return competition.getActiveSwimmers().get(0).getColor();
    }

    /**
     * Gets the stroke used in the current round.
     *
     * @return the stroke for current round.
     */
    public Stroke getStroke() {
        // Fetch stroke from the first active swimmer. 
        return competition.getActiveSwimmers().get(0).getStroke();
    }

    /**
     * Gets a list of name of active swimmers.
     *
     * @return active swimmer names.
     */
    public List<String> getActiveSwimmerNames() {
        List<String> names = new ArrayList<>();
        for (Swimmer s : competition.getActiveSwimmers()) {
            names.add(s.getName());
        }
        return names;
    }

    // What do we need to show when the competition is ongoing?
    /**
     * Gets a list of locations in the order of active swimmers.
     *
     * @return swimmer positions.
     */
    public List<Integer> getSwimmerPositions() {
        List<Integer> positions = new ArrayList<>();
        for (double pos : competition.getPositions()) {
            // Approximate location to the next smallest pixel. 
            // Take minimum with finalX, so swimmers won't end up going farther.
            positions.add((int) Math.min(finalX, Math.ceil(pos * lengthFactor)));
        }
        return positions;
    }

    /**
     * Gets the current swimmer ranking.
     *
     * @return get ranking.
     */
    public List<String> getRanking() {
        List<String> swimmerNames = new ArrayList<>();
        for (Swimmer swimmer : competition.getRanking()) {
            swimmerNames.add(swimmer.getName());
        }
        return swimmerNames;
    }

    /**
     * Gets the times taken by swimmers to finish the round in the ordered by rank.
     *
     * @return finish times.
     */
    public List<String> getTimesTaken() {
        Date startTime = competition.getStartTime();
        List<String> formattedTimes = new ArrayList<>();

        for (Date date : competition.getTimecard()) {
            formattedTimes.add(formatTimeSpan(date.getTime() - startTime.getTime()));
        }
        return formattedTimes;
    }

    /**
     * Gets the final position of swimmers in the GUI.
     *
     * @return the farthest position in the GUI.
     */
    public int getFinalX() {
        return finalX;
    }

    /**
     * Gets the number of previous rounds.
     *
     * @return previous round count.
     */
    public int getOldTimecardCount() {
        return competition.getOldScoreboardCount();
    }

    /**
     * Fetches previously saved data if any.
     */
    public void refreshScoreboard() {
        scoreboardUpdater.updateUI("refresh");
    }

    /**
     * Feedback function to show old data.
     */
    public void getOldData() {
        scoreboardUpdater.updateUI("show");
    }

    /**
     * Gets the ranking in the ith round.
     *
     * @param i round index from the beginning.
     * @return ranking in the ith round.
     */
    public List<String> getOldRanking(int i) {
        List<String> swimmerNames = new ArrayList<>();
        for (Swimmer swimmer : competition.getOldRanking(i)) {
            swimmerNames.add(swimmer.getName());
        }
        return swimmerNames;
    }

    /**
     * Gets times taken in the ith round.
     *
     * @param i round index from the beginning.
     * @return times taken by each swimmer to complete ith round.
     */
    public List<String> getOldTimesTaken(int i) {
        Date startTime = competition.getOldStartTime(i);
        List<String> formattedTimes = new ArrayList<>();

        for (Date date : competition.getOldTimecard(i)) {
            formattedTimes.add(formatTimeSpan(date.getTime() - startTime.getTime()));
        }
        return formattedTimes;
    }

    private String formatTimeSpan(long timeSpan) {
        long h = timeSpan / 3600000;
        long m = (timeSpan - h * 3600000) / 60000;
        long s = (timeSpan - h * 3600000 - m * 60000) / 1000;
        long ms = (timeSpan - h * 3600000 - m * 60000 - s * 1000);
        // hours:minutes:seconds:milliseconds
        return String.format("%d:%02d:%02d:%03d", h, m, s, ms);
    }

    /**
     * Gets whether creation of a new round is possible.
     *
     * @return whether creation of a new round is possible.
     */
    public boolean canPrepare() {
        return competition.getState() == CompetitionState.FINISHED
               || competition.getState() == CompetitionState.INITIAL;
    }

    /**
     * Prepares the competition for a new round.
     *
     * @param gender gender for the new round.
     * @param stroke stroke to be used in the new round.
     * @param swimmers list of swimmers to participate in the new round.
     */
    public void prepare(Gender gender, Stroke stroke, List<Swimmer> swimmers) {
        competition.prepare(gender, stroke, swimmers);
        generalUpdater.updateUI("buttons");
        message = "Click start to start the competition. ";
        generalUpdater.updateUI("message");
        generalUpdater.updateUI("poolbegin");
        scoreboardUpdater.updateUI("clear");
    }

    /**
     * Gets whether starting of the competition is possible.
     *
     * @return whether starting of this competition is possible.
     */
    public boolean canStart() {
        return competition.getState() == CompetitionState.READY;
    }

    /**
     * Starts the competition.
     */
    public void start() {
        competition.start();
        generalUpdater.updateUI("buttons");
        scoreboardUpdater.updateUI("roundbegin");
        message = "Wait until the competition round is finished. ";
        generalUpdater.updateUI("message");
        startPullingData();
    }

    private void startPullingData() {
        pullTimer = new Timer();
        // Start a thread to periodically update the GUI. 

        pullTimer.scheduleAtFixedRate(new TimerTask() {
            private int finishedSwimmers = 0;

            @Override
            public void run() {
                if (competition.getState() == CompetitionState.FINISHED) {
                    pullTimer.cancel();
                    generalUpdater.updateUI("buttons");
                    scoreboardUpdater.updateUI("roundend");
                    generalUpdater.updateUI("poolend");
                    message = "Click 'New Round' to get started. ";
                    generalUpdater.updateUI("message");
                }
                if (finishedSwimmers < competition.getRanking().size()) {
                    finishedSwimmers++;
                    scoreboardUpdater.updateUI("swimmers");
                }
                poolUpdater.updateUI("swimmers");

            }
        }, 0, refreshFactor);

    }

    /**
     * Returns whether the competition can be saved.
     *
     * @return whether the competition can be saved.
     */
    public boolean canSave() {
        return competition.getState() == CompetitionState.INITIAL
               || competition.getState() == CompetitionState.FINISHED;
    }

    /**
     * Serializes the competition into a file.
     *
     * @param filePath path to save competition.
     */
    public void save(String filePath) {
        Persistence.saveFile(competition, filePath);
    }

}
