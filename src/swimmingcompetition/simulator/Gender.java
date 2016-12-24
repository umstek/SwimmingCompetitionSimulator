/*
 * Gender.java
 * Enables different swimmer objects to have different genders. 
 * 
 */
package swimmingcompetition.simulator;

import java.io.Serializable;

/**
 *
 * @author Wickramaranga
 */
public enum Gender implements Serializable {

    /**
     * Represents gender (male) of a swimmer.
     */
    MALE,
    /**
     * Represents gender (female) of a swimmer.
     */
    FEMALE;
} 
// TODO try to remove this and use instanceof.
