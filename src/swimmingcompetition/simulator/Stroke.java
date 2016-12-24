/*
 * Stroke.java
 * Enumerated the different swimming styles and their relative speed. 
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;

/**
 *
 * @author Wickramaranga
 */
public enum Stroke implements Serializable  {

    FREESTYLE(0.89), // 1/45 <- some world record 44.xx seconds. 
    BUTTERFLYSTROKE(0.82), // 1/49
    BREASTSTROKE(0.71), // 1/56
    BACKSTROKE(0.82); // 1/49

    private Stroke(double factor) {
        this.strokeFactor = factor;
    }
    private final double strokeFactor;

    public double getStrokeFactor() {
        return strokeFactor;
    }

}
