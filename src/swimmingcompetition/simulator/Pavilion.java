/*
 * Pavilion.java
 * Represents the pavilion where spectators reside. 
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;
import java.util.List;

import swimmingcompetition.simulator.exceptions.WrongPersonCountException;

/**
 *
 * @author Wickramaranga
 */
public class Pavilion implements Serializable  {

    private final List<Spectator> spectators;

    /**
     * Creates an object to represent the pavilion.
     *
     * @param spectators List of spectators for the pavilion.
     */
    public Pavilion(List<Spectator> spectators) {
        if (spectators == null) {
            throw new WrongPersonCountException();
        }
        this.spectators = spectators;
    }

    /**
     * Returns a list of spectators in the competition.
     *
     * @return a list of spectators.
     */
    public List<Spectator> getSpectators() {
        return spectators;
    }

}
