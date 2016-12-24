/*
 * SwimmingLane.java
 * Represents a lane in the swimming pool where only one swimmer swims.
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wickramaranga
 */
public class SwimmingLane implements Serializable {

    private final int length;

    private Swimmer swimmer;
    private Scoreboard scoreboard;

    private volatile double swimmerPosition; // Thread-safe

    /**
     * Creates a swimming lane.
     *
     * @param length Length of the swimming pool
     */
    public SwimmingLane(int length) {
        this.length = length;
    }

    /**
     * Sets a reference to the round scoreboard.
     *
     * @param scoreboard Scoreboard for the round.
     */
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    /**
     * Sets the swimmer to swim in this lane. Set to null if the lane is not
     * used.
     *
     * @param swimmer The swimmer who swims in this lane.
     */
    public void setSwimmer(Swimmer swimmer) {
        this.swimmer = swimmer;
        this.swimmerPosition = 0;
    }

    /**
     * Gets the swimmer who currently swims in this lane.
     *
     * @return The swimmer who swims in the lane.
     */
    public Swimmer getSwimmer() {
        return swimmer;
    }

    /**
     * Gets whether this lane is used currently.
     *
     * @return whether this lane is used for the round.
     */
    public boolean isUsed() {
        return swimmer != null;
    }

    /**
     * Starts the swimmer in the lane using a new thread.
     */
    public void start() {
        Thread runner = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Adjust two constants to make it run not more than 1000 
                // maintaing natural feeling. What is swim()'s max?
                while (swimmerPosition < length) {
                    swimmerPosition += (0.25) * swimmer.swim();
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SwimmingLane.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    System.out.println(swimmer.getName() + " loc: " + swimmerPosition);
                }
                touchpad();
            }
        });
        runner.start();
    }

    /**
     * Gets the current position of the swimmer in the swimming lane.
     *
     * @return Swimmers current position in the swimming lane.
     */
    public double getSwimmerPosition() {
        return swimmerPosition;
    }

    private void touchpad() {
        this.scoreboard.notifyFinish(swimmer);
    }

}
