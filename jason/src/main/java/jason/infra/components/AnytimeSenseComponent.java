package jason.infra.components;

import jason.asSemantics.TransitionSystem;
import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public class AnytimeSenseComponent extends AnytimeComponent {

    public AnytimeSenseComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
        super(centralisedAgArchAsynchronous);
    }

    public boolean canSleep() {
        return aag.getTS().canSleepSense();
    }

    public void wakeUp() {
        synchronized (aag.objSense) {
            if (sleeping) {
                sleeping = false;
                enqueueExecutor(false);
            }
        }
    }

    /*
     * public void wakeUp(boolean ts) { synchronized (aag.objSense) { if (ts ||
     * sleeping) { sleeping = false; enqueueExecutor(ts); } } }
     */

    public void enqueueExecutor(boolean ts) {
//        if (!inQueue || ts) {
//            inQueue = true;
//            aag.getExecutorSense().execute(this);
//        }
    }

    public void run() {
        aag.getTS().anytimeSense();
    }
}
