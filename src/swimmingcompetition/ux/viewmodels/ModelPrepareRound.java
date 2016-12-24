/*
 * ModelPrepareRound.java
 * Acts as the controller/viewmodel for DialogPrepareRound frame.
 * 
 */
package swimmingcompetition.ux.viewmodels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import swimmingcompetition.simulator.Gender;
import swimmingcompetition.simulator.Stroke;
import swimmingcompetition.simulator.Swimmer;

/**
 *
 * @author Wickramaranga
 */
public class ModelPrepareRound implements DialogModel {

    private static final int MIN_SWIM = 2; // Minimum selectable swimmers.

    private final List<Swimmer> allSwimmers; // Male, Female - all.
    private List<Swimmer> availableSwimmers; // Swimmers of the gender.
    private final int maxSwimmerCount; // Lane count.
    private final List<Swimmer> selectedSwimmers; // Selected swimmers.
    private Gender gender; // Gender of the first selected swimmer.
    private Stroke stroke; // Stroke selection for the round.

    private String message;

    private UIUpdater updater;

    /**
     * Creates new ModelPrepareRound object.
     *
     * @param swimmers List of all swimmers.
     * @param laneCount Total lane count.
     */
    public ModelPrepareRound(List<Swimmer> swimmers, int laneCount) {
        allSwimmers = swimmers;
        // Initially, all swimmers are selectable.
        availableSwimmers = new ArrayList<>(swimmers);
        selectedSwimmers = new ArrayList<>();
        maxSwimmerCount = laneCount;
        gender = null;
        stroke = Stroke.FREESTYLE;
        message = "";
    }

    @Override
    public boolean isValid() {
        boolean returnValue = true;
        if (getGender() == null
            || getStroke() == null
            || selectedSwimmers == null) {
            returnValue = false;
        }

        if (selectedSwimmers.size() < MIN_SWIM
            || selectedSwimmers.size() > getMaxSwimmers()) {
            returnValue = false;
        }
        for (Swimmer s : selectedSwimmers) {
            if (s.getGender() != gender) {
                returnValue = false;
            }
        }

        updater.updateUI("message");
        updater.updateUI("lists");
        return returnValue;
    }

    /**
     * Get available swimmer count.
     *
     * @return available swimmer count.
     */
    public int getAvailableSwimmerCount() {
        return availableSwimmers.size();
    }

    /**
     * Get selected swimmer count.
     *
     * @return selected swimmer count.
     */
    public int getSelectedSwimmerCount() {
        return selectedSwimmers.size();
    }

    /**
     * Gets the maximum available swimmer count.
     *
     * @return the maximum swimmer count.
     */
    public int getMaxSwimmers() {
        return maxSwimmerCount;
    }

    /**
     * Gets the string representation of the available swimmer given by the index.
     *
     * @param i index of the available swimmer.
     * @return String representation of the swimmer.
     */
    public String getAvailableSwimmer(int i) {
        return availableSwimmers.get(i).toString();
    }

    /**
     * Gets the string representation of the selected swimmer given by the index.
     *
     * @param i index of the selected swimmer.
     * @return String representation of the swimmer.
     */
    public String getSelectedSwimmer(int i) {
        return selectedSwimmers.get(i).toString();
    }

    /**
     * Gets the gender filter for the round.
     *
     * @return the gender filter of the round.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the stroke selection for the round.
     *
     * @return the stroke selection for the round.
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * Sets the stroke selection for the round.
     *
     * @param stroke the stroke to set.
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * Gets the list of selected swimmers.
     *
     * @return the list of selected swimmers.
     */
    public List<Swimmer> getSelectedSwimmers() {
        return selectedSwimmers;
    }

    private LinkedList<Swimmer> getSwimmersForAvailableIndices(int[] indices) {
        LinkedList<Swimmer> swimmers = new LinkedList<>();
        for (int i : indices) {
            swimmers.add(availableSwimmers.get(i));
        }

        return swimmers;
    }

    /**
     * Add swimmers.
     *
     * @param indices indices of swimmers to add.
     */
    public void addSwimmers(int[] indices) {
        LinkedList<Swimmer> swimmers = getSwimmersForAvailableIndices(indices);
        if (swimmers.isEmpty()) {
            return;
        }

        // TODO correct error of adding swimmers for the first time. Gender issue.
        if (selectedSwimmers.isEmpty()) {
            // Select gender automatically from the first swimmer selected. 
            gender = swimmers.get(0).getGender();
            updater.updateUI("gender");
            // Restrict addition of future swimmers only to the selected gender.
            availableSwimmers.clear();
            for (Swimmer swimmer : allSwimmers) {
                if (swimmer.getGender() == gender) {
                    availableSwimmers.add(swimmer);
                }
            }
        }

        // Move swimmers from availableSwimmers list to selectedSwimmers list.
        while (!swimmers.isEmpty()) {
            Swimmer s = swimmers.poll();
            if (availableSwimmers.contains(s)) {
                availableSwimmers.remove(s);
                selectedSwimmers.add(s);
            }
        }

        message = "";
        updater.updateUI("message");
        updater.updateUI("lists");
    }

    private LinkedList<Swimmer> getSwimmersForSelectedIndices(int[] indices) {
        LinkedList<Swimmer> swimmers = new LinkedList<>();
        for (int i : indices) {
            swimmers.add(selectedSwimmers.get(i));
        }

        return swimmers;
    }

    /**
     * Remove swimmers.
     *
     * @param indices indices of swimmers to remove.
     */
    public void removeSwimmers(int[] indices) {
        LinkedList<Swimmer> swimmers = getSwimmersForSelectedIndices(indices);
        if (swimmers.isEmpty()) {
            return;
        }

        // Move swimmers from selectedSwimmers list to availableSwimmers list.
        while (!swimmers.isEmpty()) {
            Swimmer s = swimmers.poll();
            selectedSwimmers.remove(s);
            availableSwimmers.add(s);
        }

        // If everyone is deselected then, reset the gender filter. 
        if (selectedSwimmers.isEmpty()) {
            gender = null;
            availableSwimmers = new ArrayList<>(allSwimmers);
            message = "Please select swimmers.";
            updater.updateUI("message");
            updater.updateUI("gender");
        }

        updater.updateUI("lists");
    }

    /**
     * Sets the UIUpdater.
     *
     * @param updater the updater to set.
     */
    public void setUpdater(UIUpdater updater) {
        this.updater = updater;
    }

    /**
     * Gets the informative message.
     *
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

}
