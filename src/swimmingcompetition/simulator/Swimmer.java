/*
 * Swimmer.java
 * Represents the fields/functionality that every swimmer must have regardless
 * of gender. 
 */
package swimmingcompetition.simulator;

import java.util.Random;

/**
 *
 * @author Wickramaranga
 */
public abstract class Swimmer extends Person implements Swimmable {

    private static int count = 0;

    /**
     * Gets the number of swimmers created in this instance of program.
     *
     * @return Number of swimmers created in this instance of program.
     */
    public static int getCount() {
        return count;
    }

    // TODO try whether we can remove this and just use instanceof operator.
    private final Gender gender;
    private final Color color;
    private final double playerSpeedFactor; // skill level

    // Cannot save circular association
    transient private SwimmingLane lane; // TODO Do we need bidirectional access here? 
    private Stroke stroke;
    private double roundSpeedFactor; // current condition

    /**
     * Creates abstract swimmer fields and methods.
     *
     * @param gender Gender of the swimmer. (Male/Female)
     * @param color Uniform color.
     * @param name Name of the swimmer.
     */
    public Swimmer(Gender gender, Color color, String name) {
        super(name);
        this.gender = gender;
        this.color = color;
        // Assigns a speed with a player. 1 > Speed >= 0.5
        playerSpeedFactor = (1.0 + new Random().nextDouble()) / 2.0;
        System.out.println("New " + gender.toString() + " swimmer: " + name);
        count++;

    }

    /**
     * Gets ready to swim in the given lane using current stroke.
     *
     * @param lane Lane which this swimmer uses to swim.
     * @param stroke Stroke for the current round.
     */
    protected /*+package*/ void prepare(SwimmingLane lane, Stroke stroke) {
        this.lane = lane;
        this.stroke = stroke;
        // Ensures the speed factor for the round be > -0.1 and < 0.1 
        this.roundSpeedFactor = (new Random().nextDouble() - 0.5) / 5.0;
    }

    /**
     * Performs swim and returns current velocity of the swimmer.
     *
     * @return A double representing current velocity of the swimmer.
     */
    public double swim() {
        // Random speed factor is the factor for a small amount of time.
        // (Sampling time. It is > -0.05 and < 0.05)
        double randomSpeedFactor = (new Random().nextDouble() - 0.5) / 10.0;
        return (playerSpeedFactor + roundSpeedFactor + randomSpeedFactor)
               * performStroke();
        // (fs m best) 0.65 > finalFactor >= 0.245 (bk f worst)
    }

    /**
     * Performs stroke and returns a value representing the relative speed of
     * the stroke.
     *
     * @return The speed factor resulting from the stroke.
     */
    protected double performStroke() {
        // Return double representing strokeSpeedFactor affected by gender. 
        // Overriding methods give the effect of genderSpeedFactor. 
        switch (this.stroke) {
            case FREESTYLE:
                return performFreeStroke();
            case BUTTERFLYSTROKE:
                return performButterflyStroke();
            case BREASTSTROKE:
                return performBreastStroke();
            case BACKSTROKE:
                return performBackStroke();
            default:
                assert false : this.stroke.name(); // Never happens.
                return 0.0;
        }
    }

    /* Following abstract methods allow male and female swimmers to perform 
     * strokes differently.
     */
    /**
     * Gets the gender of the swimmer.
     *
     * @return Gender of the swimmer.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the uniform color of the swimmer.
     *
     * @return Uniform color of the swimmer.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the lane in which the swimmer swims.
     *
     * @return The lane in which the swimmer swims.
     */
    public SwimmingLane getLane() {
        return lane;
    }

    /**
     * Gets the swimming stroke used by the swimmer for current round.
     *
     * @return The stroke which is used by the swimmer for the current round.
     */
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)",
                             this.getName(),
                             this.gender.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Swimmer)) {
            return false;
        } // A female swimmer and a male swimmer cannot have the same name.
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
        final Swimmer other = (Swimmer) obj;
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
