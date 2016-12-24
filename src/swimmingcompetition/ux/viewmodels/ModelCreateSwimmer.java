/*
 * ModelCreateSwimmer.java
 * Holds data of a swimmer until the actual swimmer object is created. 
 * Acts as a model for the view class DialogCreateSwimmer
 */
package swimmingcompetition.ux.viewmodels;

import java.util.regex.Pattern;

import swimmingcompetition.simulator.FemaleSwimmer;
import swimmingcompetition.simulator.Gender;
import swimmingcompetition.simulator.MaleSwimmer;
import swimmingcompetition.simulator.Swimmer;

/**
 *
 * @author Wickramaranga
 */
public class ModelCreateSwimmer implements CreatorDialogModel<Swimmer> {

    private Gender gender;
    private String name;
    private String message;

    private UIUpdater updater;

    @Override
    public boolean isValid() {

        // A valid name, and a valid gender.
        // Alphanumeric (full)names or name with initials. 
        boolean v = (name != null) && !name.equals("")
                    && Pattern.matches("((\\w+\\s)|(\\w\\.\\s))*\\w+", name)
                    && (gender == Gender.MALE || gender == Gender.FEMALE);
        message = v ? "" : "Enter alphanumeric name (with initials). ";
        updater.updateUI("Message");
        return v;
    }

    @Override
    public Swimmer getObject() {
        if (gender == Gender.MALE) {
            return new MaleSwimmer(name);
        } else if (gender == Gender.FEMALE) {
            return new FemaleSwimmer(name);
        } else {
            assert false;
            return null;
        }
    }

    /**
     * Gets the gender of this swimmer.
     *
     * @return the gender of the swimmer.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the gender of this swimmer.
     *
     * @param gender the gender to set.
     */
    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender.toUpperCase());
    }

    /**
     * Gets the name of this swimmer.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this swimmer.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the UIUpdator.
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
