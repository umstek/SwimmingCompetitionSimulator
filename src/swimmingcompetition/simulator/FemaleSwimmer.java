/*
 * FemaleSwimmer.java
 * Represents a female swimmer.
 * 
 */
package swimmingcompetition.simulator;

/**
 *
 * @author Wickramaranga
 */
public class FemaleSwimmer extends Swimmer {

    private static final double FEMALE_SPEED_FACTOR = -0.02;

    private static int count = 0;

    /**
     * Returns the number of female swimmers created so far.
     *
     * @return number of female swimmer instances.
     */
    public static int getCount() {
        return count;
    }

    /**
     * Creates an object representation for a female swimmer.
     *
     * @param name name of the swimmer.
     */
    public FemaleSwimmer(String name) {
        super(Gender.FEMALE, Color.RED, name);

        count++;
    }

    /**
     * Performs female version of freestyle.
     *
     * @return A speed factor.
     */
    @Override
    public double performFreeStroke() {
        return Stroke.FREESTYLE.getStrokeFactor() + FEMALE_SPEED_FACTOR;
    }

    /**
     * Performs female version of butterfly stroke.
     *
     * @return A speed factor.
     */
    @Override
    public double performButterflyStroke() {
        return Stroke.BUTTERFLYSTROKE.getStrokeFactor() + FEMALE_SPEED_FACTOR;
    }

    /**
     * Performs female version of breast stroke.
     *
     * @return A speed factor.
     */
    @Override
    public double performBreastStroke() {
        return Stroke.BREASTSTROKE.getStrokeFactor() + FEMALE_SPEED_FACTOR;
    }

    /**
     * Performs female version of back stroke.
     *
     * @return A speed factor.
     */
    @Override
    public double performBackStroke() {
        return Stroke.BACKSTROKE.getStrokeFactor() + FEMALE_SPEED_FACTOR;
    }

}
