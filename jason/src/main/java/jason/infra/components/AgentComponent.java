package jason.infra.components;

import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;
import jason.infra.centralised.CentralisedAgArchAsynchronous;

public abstract class AgentComponent implements Runnable {
    protected CentralisedAgArchAsynchronous ag;
    protected CentralisedAgArchAnytimeAsynchronous aag;
    protected boolean inQueue = true;
    protected boolean sleeping = false;

    public AgentComponent(CentralisedAgArchAsynchronous ag) {
        this.ag = ag;
    }

    public AgentComponent(CentralisedAgArchAnytimeAsynchronous aag) {
        this.aag = aag;
    }

    public void sleep() {
        sleeping = true;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public abstract void wakeUp();
    public abstract void enqueueExecutor(boolean ts);
    public abstract boolean canSleep();
}
