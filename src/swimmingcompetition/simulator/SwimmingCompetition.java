/*
 * SwimmingCompetition.java
 * Represents the entire swimming competition. 
 * Top level API. 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import swimmingcompetition.simulator.exceptions.IllegalOperationException;
import swimmingcompetition.simulator.exceptions.InvalidStateException;
import swimmingcompetition.simulator.exceptions.WrongPersonCountException;

/**
 *
 * @author Wickramaranga
 */
public class SwimmingCompetition implements Serializable {

    private final List<Judge> judges;
    // private final List<Spectator> spectators; // pushed inside the pavilion.
    private final List<Swimmer> swimmers;
    private final List<SupportingStaffMember> staff;

    private final Pavilion pavilion;
    private final SwimmingPool pool;
    private final List<Scoreboard> oldScoreboards;

    private Scoreboard scoreboard;

    private CompetitionState state;

    /**
     * Creates and initializes the swimming competition class.
     *
     * @param judges List of judges.
     * @param spectators List of spectators.
     * @param swimmers List of swimmers.
     * @param staff List of supporting staff members.
     * @param swimLaneCount Number of lanes in the swimming pool.
     * @param poolLength Length of the swimming pool.
     */
    public SwimmingCompetition(List<Judge> judges,
                               List<Spectator> spectators,
                               List<Swimmer> swimmers,
                               List<SupportingStaffMember> staff,
                               int swimLaneCount,
                               int poolLength) {
        System.out.println("+ Creating competition...");

        System.out.println("  Adding people...");

        if (judges == null || judges.size() < 1) {
            throw new WrongPersonCountException();
        }
        this.judges = judges;

        if (swimmers == null) {
            throw new WrongPersonCountException();
        }
        int maleCount = 0;
        int femaleCount = 0;
        for (Swimmer s : swimmers) {
            if (s.getGender() == Gender.FEMALE) {
                femaleCount++;
            } else if (s.getGender() == Gender.MALE) {
                maleCount++;
            } else {
                assert false; // Not possible. 
            }
        }
        // At least 2 males for a male only competition.
        // At lest 2 females for a female only competition.  
        if (maleCount == 1 || femaleCount == 1 || maleCount + femaleCount < 2) {
            throw new WrongPersonCountException();
        }
        this.swimmers = swimmers;

        if (staff == null) {
            throw new WrongPersonCountException();
        }
        this.staff = staff;

        System.out.println("  Creating pavilion, pool and scoreboard...");
        pavilion = new Pavilion(spectators);
        // Scoreboard initialized when getting ready for a round.
        // scoreboard = new Scoreboard();
        oldScoreboards = new ArrayList<>();
        pool = new SwimmingPool(swimLaneCount, poolLength);

        state = CompetitionState.INITIAL; // State change
        System.out.println("- New swimming competition created: "
                           + this.toString());
    }

    /**
     * Gets the lane count.
     *
     * @return lane count.
     */
    public int getLaneCount() {
        return pool.getLaneCount();
    }

    /**
     * Gets the pool length.
     *
     * @return the pool length.
     */
    public int getPoolLength() {
        return pool.getPoolLength();
    }

    /**
     * Returns a list of available judges in the competition.
     *
     * @return A list of available judges.
     */
    public List<Judge> getJudges() {
        return judges;
    }

    /**
     * Returns a list of all available spectators.
     *
     * @return A list of available spectators.
     */
    public List<Spectator> getSpectators() {
        return pavilion.getSpectators();
    }

    /**
     * A list consisting of all male and female swimmers.
     *
     * @return A list of all swimmers.
     */
    public List<Swimmer> getSwimmers() {
        return swimmers;
    }

    /**
     * A list of all supporting staff members for the competition.
     *
     * @return A list with supporting staff members.
     */
    public List<SupportingStaffMember> getStaff() {
        return staff;
    }

    /**
     * Gets a list of all people involved in this competition.
     *
     * @return A list of all people involved in the competition.
     */
    public List<Person> getAllPeople() {
        List<Person> everyone = new ArrayList<>();
        everyone.addAll(judges);
        everyone.addAll(pavilion.getSpectators());
        everyone.addAll(swimmers);
        everyone.addAll(staff);
        return everyone;
    }

    /**
     * Gets the state of the competition.
     *
     * @return the state of the competition.
     */
    public CompetitionState getState() {
        return state;
    }

    /**
     * Prepares the competition for a new round.
     *
     * @param gender gender filter.
     * @param stroke stroke selection.
     * @param swimmers a list of swimmers to participate in this competition.
     */
    public void prepare(Gender gender, Stroke stroke, List<Swimmer> swimmers) {
        if (getState() != CompetitionState.INITIAL
            && getState() != CompetitionState.FINISHED) {
            throw new InvalidStateException();
        }

        // Swimmers who have not registered in this competition are not allowed.
        for (Swimmer swimmer : swimmers) {
            if (!swimmers.contains(swimmer)) {
                throw new IllegalOperationException();
            }
        }
        // Try to add swimmers to the round. 
        // This takes care of swimmer count and gender based validation. 
        pool.prepare(gender, stroke, swimmers);

        // Keep records of rounds.                         
        if (scoreboard != null) { // scoreboard == null means first round.
            oldScoreboards.add(scoreboard);
        }
        scoreboard = new Scoreboard(swimmers.size(), new Runnable() {
            @Override // Push notification of competition ending.
            public void run() {
                stop();
                System.out.println("Round ended. ");
            }
        });
        // This looks stupid. >:(
        for (Spectator spectator : pavilion.getSpectators()) {
            scoreboard.subscribe(spectator);
        }

        pool.setScoreboard(scoreboard);

        state = CompetitionState.READY; // State change
    }

    /**
     * Starts the competition.
     *
     */
    public void start() {
        if (this.getState() != CompetitionState.READY) {
            throw new InvalidStateException();
        }

        // Just ask only the first judge to blow his whistle?
        judges.get(0).blowWhistle();

        state = CompetitionState.ONGOING; // State change
        pool.start(); // Start all swimmers. 
        //return true; // If starting was possible. 
    }

    /**
     * Gets the list of swimmers who are currently swimming in the order.
     *
     * @return The list of active swimmers in order.
     */
    public List<Swimmer> getActiveSwimmers() {
        if (state == CompetitionState.INITIAL) {
            throw new InvalidStateException();
        }
        return pool.getActiveSwimmers();
    }

    /**
     * Gets positions of swimmers.
     *
     * @return Current positions of swimmers.
     */
    public List<Double> getPositions() {
        if (!(state == CompetitionState.ONGOING
              || state == CompetitionState.FINISHED)) {
            throw new InvalidStateException();
        }
        return pool.getPositions();
    }

    /**
     * Signals stopping of the competition.
     *
     * @return Whether stopping the competition was possible.
     */
    public boolean stop() {
        if (this.getState() != CompetitionState.ONGOING) {
            throw new InvalidStateException();
        }

        state = CompetitionState.FINISHED; // State change
        return true;
    }

    /**
     * Gets the starting time of the competition.
     *
     * @return competition start time.
     */
    public Date getStartTime() {
        if (!(state == CompetitionState.ONGOING
              || state == CompetitionState.FINISHED)) {
            throw new InvalidStateException();
        }
        return scoreboard.getStartTime();
    }

    /**
     * Gets a list of finishing times.
     *
     * @return list of finishing times of the players.
     */
    public List<Date> getTimecard() {
        if (!(state == CompetitionState.ONGOING
              || state == CompetitionState.FINISHED)) {
            throw new InvalidStateException();
        }
        return scoreboard.getFinishTimes();
    }

    /**
     * Gets the ranking of the swimmers.
     *
     * @return the finishing order of the swimmers.
     */
    public List<Swimmer> getRanking() {
        if (!(state == CompetitionState.ONGOING
              || state == CompetitionState.FINISHED)) {
            throw new InvalidStateException();
        }
        return scoreboard.getSwimmerRanking();
    }

    /**
     * Gets the number of previous rounds conducted.
     *
     * @return the number of previous rounds.
     */
    public int getOldScoreboardCount() {
        return oldScoreboards.size();
    }

    /**
     * Gets the starting time of a previous round by the round index.
     *
     * @param index index of the round to be retrieved.
     * @return the start time of the round given by index.
     */
    public Date getOldStartTime(int index) {
        if (state == CompetitionState.INITIAL) {
            throw new InvalidStateException();
        }
        return oldScoreboards.get(index).getStartTime();
    }

    /**
     * Gets the finishing times of the swimmers in the given round.
     *
     * @param index index of the round to be retrieved.
     * @return the finishing times of the swimmers in the given round.
     */
    public List<Date> getOldTimecard(int index) {
        if (state == CompetitionState.INITIAL) {
            throw new InvalidStateException();
        }
        return oldScoreboards.get(index).getFinishTimes();
    }

    /**
     * Gets the ranking information of the swimmers in a previous round.
     *
     * @param index index of the round to be retrieved.
     * @return the ranking of swimmers in the given round.
     */
    public List<Swimmer> getOldRanking(int index) {
        if (state == CompetitionState.INITIAL) {
            throw new InvalidStateException();
        }
        return oldScoreboards.get(index).getSwimmerRanking();
    }

}
