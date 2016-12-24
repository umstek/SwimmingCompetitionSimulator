/*
 * CompetitionState.java
 * Helps to keep track of the state of the competition.
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;

/**
 *
 * @author Wickramaranga
 */
public enum CompetitionState implements Serializable {

    /**
     * Competition is created but a round has not yet been organized.
     */
    INITIAL,
    /**
     * Swimmers for a round has been selected and Competition is ready to start.
     */
    READY,
    /**
     * A competition round is running.
     */
    ONGOING,
    /**
     * A competition round has finished.
     */
    FINISHED;
}
