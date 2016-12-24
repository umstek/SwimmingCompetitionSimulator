/*
 * SupportingStaffMember.java
 * Represents a member of the supporting staff. 
 * 
 */
package swimmingcompetition.simulator;

/**
 *
 * @author Wickramaranga
 */
public class SupportingStaffMember extends Person {

    private static int count = 0;

    /**
     * Returns the number of supporting staff members created so far.
     *
     * @return The number of supporters created so far.
     */
    public static int getCount() {
        return count;
    }

    /**
     * Creates a new supporting staff member.
     *
     * @param name Name of the new supporting staff member.
     */
    public SupportingStaffMember(String name) {
        super(name);

        System.out.println("New supporting staff member: " + name);
        count++;
    }

}
