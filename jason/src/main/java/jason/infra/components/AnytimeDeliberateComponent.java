package jason.infra.components;

import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public class AnytimeDeliberateComponent extends AnytimeComponent {

    public AnytimeDeliberateComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
        super(centralisedAgArchAsynchronous);
    }

    public boolean canSleep() {
        return aag.getTS().canSleepDeliberate();
    }

    public void wakeUp() {
        synchronized (aag.objDeliberate) {
            if (sleeping) {
                sleeping = false;
                enqueueExecutor(false);
            }
        }
    }

    public void enqueueExecutor(boolean ts) {
//        if (!inQueue || ts) {
//            inQueue = true;
//            aag.getExecutorDeliberate().execute(this);
//        } else {
//            System.out.println("It's already in the queue! DELIBERATE");
//        }
    }

    public void run() {
        aag.getTS().anytimeDeliberate();
    }
}
