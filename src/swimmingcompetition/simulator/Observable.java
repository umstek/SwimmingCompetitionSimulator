/*
 * Observable.java
 * Used to implement observer design pattern.
 *
 */
package swimmingcompetition.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wickramaranga
 */
public abstract class Observable implements Serializable {

    protected final List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer o) {
        if (o != null) {
            observers.add(o);
        }
    }

    public void unsubscribe(Observer o) {
        if (o != null) {
            observers.remove(o);
        }
    }

    public abstract void notifyObservers(Observable o, Object arg);
}
