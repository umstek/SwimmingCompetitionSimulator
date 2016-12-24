/*
 * DialogModel.java
 * Allows a GUI frames to follow a common standard for validation.
 * 
 */
package swimmingcompetition.ux.viewmodels;

/**
 *
 * @author Wickramaranga
 */
public interface DialogModel {

    /**
     * Returns whether model class has valid status.
     *
     * @return whether state of the model is valid.
     */
    boolean isValid();
}
