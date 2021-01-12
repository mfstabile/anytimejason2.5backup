package jason.infra.components;

import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public class AnytimeDeliberateProfilerComponent extends AnytimeComponent {

    double timeLimit = 0;
    
    public AnytimeDeliberateProfilerComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
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
        int cycles = aag.getCyclesDeliberate();
        //int number_cycles = 1;
//        int i = 0;
//
//        while (aag.isRunning() && i < cycles) {
//            i++;
//
            aag.getTS().anytimeDeliberateProfiler();
//
//            synchronized (aag.objDeliberate) {
//
//                if (canSleep()) {
//                    inQueue = false;
//                    sleep();
//                    return;
//                } else if (i == cycles) {
//                    enqueueExecutor(true);
//                    return;
//                }
//            }
//        }
    }

}
