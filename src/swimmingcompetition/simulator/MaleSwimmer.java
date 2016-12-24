/*
 * MaleSwimmer.java
 * Represents a male swimmer. 
 * 
 */
package swimmingcompetition.simulator;

/**
 *
 * @author Wickramaranga
 */
public class MaleSwimmer extends Swimmer {

    private static final double MALE_SPEED_FACTOR = +0.02;

    private static int count = 0;

    /**
     * Returns the number of male swimmers created so far.
     *
     * @return number of male swimmer instances.
     */
    public static int getCount() {
        return count;
    }

    /**
     * Creates an object representation for a male swimmer.
     *
     * @param name name of the swimmer.
     */
    public MaleSwimmer(String name) {
        super(Gender.MALE, Color.BLUE, name);

        count++;
    }

    /**
     * Performs male version of freestyle.
     *
     * @return A speed factor.
     */
    @Override
    public double performFreeStroke() {
        return Stroke.FREESTYLE.getStrokeFactor() + MALE_SPEED_FACTOR;
    }

    /**
     * Performs male version of butterfly stroke.
     *
     * @return A speed factor.
     */
    @Override
    public double performButterflyStroke() {
        return Stroke.BUTTERFLYSTROKE.getStrokeFactor() + MALE_SPEED_FACTOR;
    }

    /**
     * Performs male version of breast stroke.
     *
     * @return A speed factor.
     */
    @Override
    public double performBreastStroke() {
        return Stroke.BREASTSTROKE.getStrokeFactor() + MALE_SPEED_FACTOR;
    }

    /**
     * Performs male version of back stroke.
     *
     * @return A speed factor.
     */
    @Override
    public double performBackStroke() {
        return Stroke.BACKSTROKE.getStrokeFactor() + MALE_SPEED_FACTOR;
    }
}
