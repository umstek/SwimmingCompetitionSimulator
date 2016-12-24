/*
 * Person.java
 * Represents the properties/functionality that any person should have according
 * to the abstraction of swimming competition. 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;

/**
 *
 * @author Wickramaranga
 */
public abstract class Person implements Serializable {

    private static int count = 0;

    /**
     * Gets the number of all people created in this instance of program.
     *
     * @return Number of all persons created in this instance of program.
     */
    public static int getCount() {
        return count;
    }

    private final String name;

    /**
     * Creates a person object.
     *
     * @param name Name of the person
     */
    public Person(String name) {
        this.name = name;

        count++;
    }

    /**
     * Gets the name of the person.
     *
     * @return Name of the person.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Person)) {
            return false;
        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
        final Person other = (Person) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
