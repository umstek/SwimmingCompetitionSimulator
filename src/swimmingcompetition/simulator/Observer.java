/*
 * Observer.java
 * used to implement observer design pattern. 
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;

/**
 *
 * @author Wickramaranga
 */
public interface Observer extends Serializable {

    void update(Observable sender, Object arg);
}
