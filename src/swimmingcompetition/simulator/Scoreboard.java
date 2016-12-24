/*
 * Scoreboard.java
 * Represents the scoreboard where player times are compared and the winner is 
 * displayed. 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Wickramaranga
 */
public class Scoreboard extends Observable implements Serializable {

    private final int numberOfSwimmers;
    transient private final Runnable stopCallback; // Cannot save a method.
    // stopCallback's Run method is executed at the end of the competition. 

    private Date startTime;
    private final List<Date> finishTimes;
    private final List<Swimmer> finishSwimmers;

    public Scoreboard(int numberOfSwimmers, Runnable stopCallback) {
        this.numberOfSwimmers = numberOfSwimmers;
        this.stopCallback = stopCallback;
        finishTimes = new ArrayList<>();
        finishSwimmers = new ArrayList<>();
    }

    /**
     * Gets the number of swimmers participating in this round.
     *
     * @return the numberOfSwimmers
     */
    public int getNumberOfSwimmers() {
        return numberOfSwimmers;
    }

    /**
     * Notifies scoreboard of the starting of the competition.
     */
    public void notifyStart() {
        startTime = new Date();
    }

    /**
     * Returns the time when the competition started.
     *
     * @return The time when competition started.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Notified the scoreboard that a swimmer just finished the game. So, swimmers are guaranteed to
     * be in the winning order in FinishTimes.
     *
     * @param swimmer Swimmer who finished now.
     */
    public synchronized void notifyFinish(Swimmer swimmer) {
        assert swimmer != null; // Dead men can't talk ;)

        // Swimmer finished now! IMPORTANT: Add time first!! 
        finishTimes.add(new Date());
        finishSwimmers.add(swimmer);

        notifyObservers(this, swimmer);
        tryEndCompetition(); // Check whether all have crossed the finish line.
    }

    private void tryEndCompetition() {
        // Notify the competition saying that the competition has ended. 
        if (finishTimes.size() == numberOfSwimmers) {
            new Thread(stopCallback).start();
        }
    }

    /**
     * Returns finish times for all participated swimmers in the winning order.
     *
     * @return All swimmers and their finish times.
     */
    public synchronized List<Date> getFinishTimes() {
        return finishTimes;
    }

    /**
     * Returns the swimmer profile for the rank.
     *
     * @return Swimmer object corresponding to the index.
     */
    public synchronized List<Swimmer> getSwimmerRanking() {
        return finishSwimmers;
    }

    @Override
    public void notifyObservers(Observable o, Object arg) {
        for (Observer observer : observers) {
            observer.update(o, arg);
        }

    }

}
