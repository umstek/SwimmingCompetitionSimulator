/*
 * Judge.java
 * Represents the judge, who starts the swimming competition. 
 * 
 */
package swimmingcompetition.simulator;

/**
 *
 * @author Wickramaranga
 */
public class Judge extends Person {

    private static int count = 0;

    /**
     * Returns total number of judge objects created during the lifetime of the
     * program.
     *
     * @return number of judge instances created so far.
     */
    public static int getCount() {
        return count;
    }

    /**
     * Creates an object to represent a judge.
     *
     * @param name name of the judge.
     */
    public Judge(String name) {
        super(name);

        System.out.println("New judge: " + name);
        count++;
    }

    /**
     * Signals that a competition round has just been started.
     */
    public void blowWhistle() {
        System.out.println(this.getName() + ": Whistle...");
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
        final Judge other = (Judge) obj;
        return super.equals(other);
    }

}
