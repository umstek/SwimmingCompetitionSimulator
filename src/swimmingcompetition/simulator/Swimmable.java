/*
 * Swimmable.java
 * Provides a class (an object of a class) the ability to swim in this 
 * simulation. 
 */
package swimmingcompetition.simulator;

/**
 *
 * @author Wickramaranga
 */
public interface Swimmable {
// All methods are implicitly public and abstract. 

    double performFreeStroke();

    double performButterflyStroke();

    double performBreastStroke();

    double performBackStroke();
}
