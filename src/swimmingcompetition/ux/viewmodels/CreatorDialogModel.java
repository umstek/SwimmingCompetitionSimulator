/*
 * CreatorDialogModel.java
 * Allows creating viewmodels for creating different types of objects used in
 * swimming competition simulator.
 */
package swimmingcompetition.ux.viewmodels;

/**
 *
 * @author Wickramaranga
 * @param <T> Type of the underlying model.
 */
public interface CreatorDialogModel<T> extends DialogModel {

    /**
     * Returns the underlying object of the model.
     *
     * @return underlying object.
     */
    T getObject();

}
