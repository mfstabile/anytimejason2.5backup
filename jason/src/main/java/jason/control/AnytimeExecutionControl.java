package jason.control;


/**
 * Base class for the user implementation of execution control.
 *
 * <p>This default implementation synchronise the agents execution, i.e.,
 * each agent will perform its next reasoning cycle only when all agents have
 * finished its reasoning cycle.
 *
 * <p>Execution sequence:
 *    <ul><li>setExecutionControlInfraTier,
 *        <li>init,
 *        <li>(receivedFinishedCycle)*,
 *        <li>stop.
 *    </ul>
 */
public class AnytimeExecutionControl extends ExecutionControl{


    /** Called when all agents have finished the current cycle */
    protected void allAgsFinished() {
        startNewCycle();
//        infraControl.informAllAgsToPerformCycle(getCycleNumber());
        logger.fine("starting cycle "+getCycleNumber());
    }

}
