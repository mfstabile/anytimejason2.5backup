package jason.infra.components;

import java.util.logging.Level;
import java.util.logging.Logger;

import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public abstract class AnytimeComponent extends AgentComponent {

    public AnytimeComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
        super(centralisedAgArchAsynchronous);
    }

    public abstract void wakeUp();
    
    public abstract void enqueueExecutor(boolean ts);

    public abstract boolean canSleep();

    public abstract void run();
    
    private Object  syncMonitor                = new Object();
    private volatile boolean inWaitSyncMonitor = false;
    protected Logger         logger  = Logger.getLogger(CentralisedAgArch.class.getName());
    
    /**
     * waits for a signal to continue the execution (used in synchronised
     * execution mode)
     */
    protected void waitSyncSignal() {
        try {
            synchronized (syncMonitor) {
                inWaitSyncMonitor = true;
                syncMonitor.wait();
                inWaitSyncMonitor = false;
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error waiting sync (1)", e);
        }
    }

    /**
     * inform this agent that it can continue, if it is in sync mode and
     * waiting a signal
     */
    public void receiveSyncSignal() {
        try {
            synchronized (syncMonitor) {
                while (!inWaitSyncMonitor) {
                    // waits the agent to enter in waitSyncSignal
                    syncMonitor.wait(50);
                }
                syncMonitor.notifyAll();
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error waiting sync (2)", e);
        }
    }
    
}
