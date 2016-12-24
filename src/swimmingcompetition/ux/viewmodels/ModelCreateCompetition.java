/*
 * ModelCreateCompetition.java
 * Holds data of a competition until the respective competition object is created. 
 * Acts as a controller/viewmodel for the DialogCreateCompetition class.
 */
package swimmingcompetition.ux.viewmodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import swimmingcompetition.simulator.Gender;
import swimmingcompetition.simulator.Judge;
import swimmingcompetition.simulator.Person;
import swimmingcompetition.simulator.Spectator;
import swimmingcompetition.simulator.SupportingStaffMember;
import swimmingcompetition.simulator.Swimmer;
import swimmingcompetition.simulator.SwimmingCompetition;

/**
 *
 * @author Wickramaranga
 */
public class ModelCreateCompetition implements CreatorDialogModel<SwimmingCompetition> {

    private static final int MIN_SWIM = 2; // Minimum swimmer count for a gender.
    private static final int MAX_POOL = 1000; // Maximum possible length for the pool.
    private static final int MIN_POOL = 5; // Minimum possible length for the pool.
    private static final int DEFAULT_POOL = 25; // Default length for the pool. 
    private static final int DEFAULT_LANES = 5; // Default lane count;

    private final List<Judge> judges;
    private final List<Spectator> spectators;
    private final List<Swimmer> swimmers;
    private final List<SupportingStaffMember> staff;
    private int swimLaneCount;
    private int poolLength;
    private String message;

    private UIUpdater updater;

    /**
     * Creates a model for the window DialogCreateCompetition.
     */
    public ModelCreateCompetition() {
        judges = new ArrayList<>();
        spectators = new ArrayList<>();
        swimmers = new ArrayList<>();
        staff = new ArrayList<>();
        swimLaneCount = DEFAULT_LANES;
        poolLength = DEFAULT_POOL;
        message = "";
    }

    @Override
    public boolean isValid() {
        boolean judgePresent = judges.size() > 0;
        int maleSwimmerCount = 0;
        int femaleSwimmerCount = 0;
        for (Swimmer swimmer : swimmers) {
            if (swimmer.getGender() == Gender.MALE) {
                maleSwimmerCount++;
            } else if (swimmer.getGender() == Gender.FEMALE) {
                femaleSwimmerCount++;
            } else {
                assert false; // Neither male, nor female. Not supposed to happen. 
                return false;
            }
        }

        boolean valid = judgePresent
                        && ((femaleSwimmerCount == 0 && maleSwimmerCount >= MIN_SWIM)
                            || (maleSwimmerCount == 0 && femaleSwimmerCount >= MIN_SWIM)
                            || (maleSwimmerCount >= MIN_SWIM && femaleSwimmerCount >= MIN_SWIM))
                        && swimLaneCount >= MIN_SWIM
                        && poolLength >= MIN_POOL && poolLength <= MAX_POOL;
        message = valid ? "" : "Add at least 2 swimmers with the same gender and a judge. ";
        updater.updateUI("Message");
        return valid;
    }

    @Override
    public SwimmingCompetition getObject() {
        return new SwimmingCompetition(judges, spectators, swimmers, staff,
                                       swimLaneCount, poolLength);
    }

    /**
     * Gets the number of lanes in the competition.
     *
     * @return the number of lanes in this SwimmingCompetition.
     */
    public int getSwimLaneCount() {
        return swimLaneCount;
    }

    /**
     * Sets the number of lanes in this SwimmingCompetition.
     *
     * @param swimLaneCount the number of lanes in this competition.
     */
    public void setSwimLaneCount(int swimLaneCount) {
        this.swimLaneCount = swimLaneCount;
    }

    /**
     * Gets the length of the SwimmingPool.
     *
     * @return the length of this pool in this competition.
     */
    public int getPoolLength() {
        return poolLength;
    }

    /**
     * Sets the length of this SwimmingCompetition.
     *
     * @param poolLength the poolLength to set.
     */
    public void setPoolLength(int poolLength) {
        // Minimum poolLength is 5 meters while the maximum is 1000 meters.
        if (poolLength < MIN_POOL) {
            this.poolLength = MIN_POOL;
        } else if (poolLength > MAX_POOL) {
            this.poolLength = MAX_POOL;
        } else {
            this.poolLength = poolLength;
        }
        updater.updateUI("PoolLength");
    }

    /**
     * Add a person to the competition.
     *
     * @param person any person to add to the competition.
     * @return whether the person object was added to the competition successfully.
     */
    public boolean addPerson(Person person) {
        String update;
        boolean add;

        if (person instanceof Swimmer) {
            update = "Swimmer";
            if (swimmers.contains((Swimmer) person)) {
                return false;
            }
            add = swimmers.add((Swimmer) person);
        } else if (person instanceof Judge) {
            if (judges.contains((Judge) person)) {
                return false;
            }
            update = "Judge";
            add = judges.add((Judge) person);
        } else if (person instanceof Spectator) {
            if (spectators.contains((Spectator) person)) {
                return false;
            }
            update = "Spectator";
            add = spectators.add((Spectator) person);
        } else if (person instanceof SupportingStaffMember) {
            if (staff.contains((SupportingStaffMember) person)) {
                return false;
            }
            update = "SupportingStaffMember";
            add = staff.add((SupportingStaffMember) person);
        } else {
            assert false; // Not supposed to happen.
            add = false;
            update = "Person";
        }

        updater.updateUI(update);
        return add;
    }

    /**
     * Remove a person from the competition.
     *
     * @param person the person to remove from the competition.
     * @return whether removing the person was successful.
     */
    public boolean removePerson(Person person) {
        if (person instanceof Swimmer) {
            updater.updateUI("Swimmer");
            return swimmers.remove((Swimmer) person);
        } else if (person instanceof Judge) {
            updater.updateUI("Judge");
            return judges.remove((Judge) person);
        } else if (person instanceof Spectator) {
            updater.updateUI("Spectator");
            return spectators.remove((Spectator) person);
        } else if (person instanceof SupportingStaffMember) {
            updater.updateUI("SupportingStaffMember");
            return staff.remove((SupportingStaffMember) person);
        } else {
            assert false; // Not supposed to happen. 
            updater.updateUI("Person");
            return false;
        }
    }

    /**
     * Gets the swimmer count.
     *
     * @return the number of swimmers currently in this competition.
     */
    public int getSwimmerCount() {
        return swimmers.size();
    }

    /**
     * Gets the number of judges in this competition.
     *
     * @return the number of judges currently in this competition.
     */
    public int getJudgesCount() {
        return judges.size();
    }

    /**
     * Gets the number of spectators in this competition.
     *
     * @return the number of spectators in this competition.
     */
    public int getSpectatorCount() {
        return spectators.size();
    }

    /**
     * Gets the number of supporting staff members in this competition.
     *
     * @return the number of supporting staff members currently in this competition.
     */
    public int getSupportingStaffMemberCount() {
        return staff.size();
    }

    /**
     * Gets the string representation of a swimmer by index.
     *
     * @param index index of the swimmer to get the name of.
     * @return the string representation [name (gender)] of the swimmer who is in the current index.
     */
    public String getSwimmer(int index) {
        return swimmers.get(index).toString();
    }

    /**
     * Gets the string representation of a judge by index.
     *
     * @param index index of the judge to retrieve.
     * @return the string representation of the judge.
     */
    public String getJudge(int index) {
        return judges.get(index).toString();
    }

    /**
     * Gets the string representation of a supporting staff member by index.
     *
     * @param index the index of the supporting staff member to retrieve.
     * @return the string representation of the staff member.
     */
    public String getSupportingStaffMember(int index) {
        return staff.get(index).toString();
    }

    /**
     * Gets the string representation of a spectator by index.
     *
     * @param index the index of the spectator to retrieve.
     * @return the string representation of the spectator to retrieve.
     */
    public String getSpectator(int index) {
        return spectators.get(index).toString();
    }

    /**
     * Removes a list of swimmers by their indices.
     *
     * @param indices indices of swimmers to remove.
     */
    public void removeSwimmers(int[] indices) {
        int removed = 0;
        Arrays.sort(indices);
        for (int i : indices) {
            swimmers.remove(i - removed++);
        }
        updater.updateUI("Swimmer");
    }

    /**
     * Removes a list of judges by their indices.
     *
     * @param indices indices of judges to remove.
     */
    public void removeJudges(int[] indices) {
        int removed = 0;
        Arrays.sort(indices);
        for (int i : indices) {
            judges.remove(i - removed++);
        }
        updater.updateUI("Judge");
    }

    /**
     * Remove a list of spectators by their indices.
     *
     * @param indices indices of judges to remove.
     */
    public void removeSpectators(int[] indices) {
        int removed = 0;
        Arrays.sort(indices);
        for (int i : indices) {
            spectators.remove(i - removed++);
        }
        updater.updateUI("Spectator");
    }

    /**
     * Remove a list of supporting staff members by their indices.
     *
     * @param indices indices of supporting staff members to remove.
     */
    public void removeSupportingStaffMembers(int[] indices) {
        int removed = 0;
        Arrays.sort(indices);
        for (int i : indices) {
            staff.remove(i - removed++);
        }
        updater.updateUI("SupportingStaffMember");
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
     * Gets the information message.
     *
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

}
