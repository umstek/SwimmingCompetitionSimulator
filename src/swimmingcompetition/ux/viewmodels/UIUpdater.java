/*
 * UIUpdater.java
 * Enables required ViewModel classes to update user interface when necessary.
 * 
 */
package swimmingcompetition.ux.viewmodels;

/**
 *
 * @author Wickramaranga
 */
public interface UIUpdater {

    /**
     * Update the user interface.
     *
     * @param partToUpdate
     */
    void updateUI(String partToUpdate);
}
