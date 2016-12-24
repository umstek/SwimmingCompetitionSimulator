/*
 * Spectator.java
 * Represents a spectator. 
 * 
 */
package swimmingcompetition.simulator;

/**
 *
 * @author Wickramaranga
 */
public class Spectator extends Person implements Observer {

    private static int count = 0;

    /**
     * Returns the number of spectators created so far.
     *
     * @return The number of spectators created so far.
     */
    public static int getCount() {
        return count;
    }

    /**
     * Creates a spectator.
     *
     * @param name Name of the spectator to be created.
     */
    public Spectator(String name) {
        super(name);

        System.out.println("New spectator: " + name);
        count++;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Spectator other = (Spectator) obj;
        return super.equals(other);
    }

    @Override
    public void update(Observable sender, Object arg) {
        System.out.println(this.getName() + " recieved finishing of " + arg.toString());
    }

}
