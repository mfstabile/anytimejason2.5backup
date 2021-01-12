package jason.infra.components;

import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public class AnytimeActComponent extends AnytimeComponent {

    public AnytimeActComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
        super(centralisedAgArchAsynchronous);
    }

    public void wakeUp() {
        synchronized (aag.objAct) {
            if (sleeping) {
                sleeping = false;
                enqueueExecutor(false);
            }
        }
    }

    public void enqueueExecutor(boolean ts) {
//        if (!inQueue || ts) {
//            inQueue = true;
//            aag.getExecutorAct().execute(this);
//        }
    }

    public boolean canSleep() {
        return aag.getTS().canSleepAct();
    }

    public void run() {
        aag.getTS().anytimeAct();
    }
}
