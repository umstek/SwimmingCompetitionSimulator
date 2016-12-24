/*
 * SwimmingPool.java
 * Represents the swimming pool to be used in the swimming competition. 
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import swimmingcompetition.simulator.exceptions.IllegalOperationException;
import swimmingcompetition.simulator.exceptions.WrongPersonCountException;

/**
 *
 * @author Wickramaranga
 */
public class SwimmingPool implements Serializable {

    private final SwimmingLane[] lanes;
    private final int poolLength;

    private Scoreboard scoreboard;

    /**
     * Creates a new swimming pool for the competition.
     *
     * @param laneCount Maximum lanes in this swimming pool.
     * @param poolLength Length of the swimming pool.
     */
    public SwimmingPool(int laneCount, int poolLength) {
        this.lanes = new SwimmingLane[laneCount];
        for (int i = 0; i < laneCount; i++) {
            this.lanes[i] = new SwimmingLane(poolLength);
        }
        this.poolLength = poolLength;
    }

    public int getLaneCount() {
        return lanes.length;
    }

    /**
     * Length of the pool.
     *
     * @return the poolLength
     */
    public int getPoolLength() {
        return poolLength;
    }

    /**
     * Sets the scoreboard to be used in the current round for all lanes.
     *
     * @param scoreboard
     */
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        for (SwimmingLane lane : lanes) {
            lane.setScoreboard(scoreboard);
        }
    }

    /**
     * Sets swimmers for the current round.
     *
     * @param genderFilter The gender of the swimmers in the competition round.
     * @param stroke The stroke which is to be used by swimmers.
     * @param swimmers A list of participants.
     */
    public void prepare(Gender genderFilter,
                        Stroke stroke,
                        List<Swimmer> swimmers) {
        if (swimmers == null) {
            throw new WrongPersonCountException();
            // A list of swimmers must be specified. 
        }
        if (swimmers.size() > lanes.length || swimmers.size() < 2) {
            throw new WrongPersonCountException();
            // At least two (2) swimmers are needed for a round. 
            // Cannot have swimmers more than the maximum available lane count. 
        }

        for (int i = 0; i < swimmers.size(); i++) {
            Swimmer swimmer = swimmers.get(i);
            if (swimmer.getGender() != genderFilter) {
                throw new IllegalOperationException();
                // Cannot have competition rounds with genders mixed. 
            }
            swimmer.prepare(lanes[i], stroke);
            lanes[i].setSwimmer(swimmer);
        }
        // Nullify swimmers for unused lanes. 
        for (int i = swimmers.size(); i < lanes.length; i++) {
            lanes[i].setSwimmer(null);
        }
    }

    /**
     * Gets a list of all swimmers in the round.
     *
     * @return A list of all swimmers in the round.
     */
    public List<Swimmer> getActiveSwimmers() {
        List<Swimmer> activeSwimmers = new ArrayList<>();
        for (SwimmingLane lane : lanes) {
            if (lane.isUsed()) {
                activeSwimmers.add(lane.getSwimmer());
            }
        }
        return activeSwimmers;
    }

    /**
     * Tells all swimmers to start swimming.
     */
    public void start() {
        scoreboard.notifyStart();
        for (SwimmingLane lane : lanes) {
            if (lane.isUsed()) { // Ask to start only if the lane is used. 
                lane.start();
            }
        }
    }

    /**
     * Gets the positions of swimmers.
     *
     * @return Positions of the swimmers in order.
     */
    public List<Double> getPositions() {
        List<Double> positions = new ArrayList<>();
        for (SwimmingLane lane : lanes) {
            positions.add(lane.getSwimmerPosition());
        }
        return positions;
    }

}
