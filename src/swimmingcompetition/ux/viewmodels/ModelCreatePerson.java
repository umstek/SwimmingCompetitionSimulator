/*
 * ModelCreatePerson.java
 * Holds data of a person until the respective person object is created.
 * Acts as a model for the view class DialogCreatePerson. 
 */
package swimmingcompetition.ux.viewmodels;

import java.util.regex.Pattern;

import swimmingcompetition.simulator.Judge;
import swimmingcompetition.simulator.Person;
import swimmingcompetition.simulator.Spectator;
import swimmingcompetition.simulator.SupportingStaffMember;

/**
 *
 * @author Wickramaranga
 */
public class ModelCreatePerson implements CreatorDialogModel<Person> {

    private Class role;
    private String name;
    private String message;

    private UIUpdater updater;

    public ModelCreatePerson() {
        this.role = Spectator.class;
        this.name = "";
        this.message = "";
    }

    /**
     * Get the role of this person.
     *
     * @return the role of the person.
     */
    public Class getRole() {
        return role;
    }

    /**
     * Sets the role of this person.
     *
     * @param role the role to set.
     */
    public void setRole(String role) {
        switch (role) {
            case "Judge":
                this.role = Judge.class;
                break;
            case "Spectator":
                this.role = Spectator.class;
                break;
            case "Supporting Staff Member":
                this.role = SupportingStaffMember.class;
                break;
            default:
                assert false;
                this.role = null;
                break;
        }
    }

    /**
     * Gets the name of the person.
     *
     * @return the name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this person.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Person getObject() {
        if (Judge.class.equals(role)) {
            return new Judge(name);
        } else if (Spectator.class.equals(role)) {
            return new Spectator(name);
        } else if (SupportingStaffMember.class.equals(role)) {
            return new SupportingStaffMember(name);
        } else {
            assert false;
            return null;
        }
    }

    @Override
    public boolean isValid() {
        boolean v = (name != null) && !name.equals("") && role != null
                    && Pattern.matches("((\\w+\\s)|(\\w\\.\\s))*\\w+", name)
                    && (role.equals(Judge.class) || role.equals(Spectator.class)
                        || role.equals(SupportingStaffMember.class));
        message = v ? "" : "Enter alphanumeric name (with initials). ";
        updater.updateUI("Message");
        return v;
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
