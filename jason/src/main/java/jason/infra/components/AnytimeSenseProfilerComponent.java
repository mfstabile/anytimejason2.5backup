package jason.infra.components;

import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public class AnytimeSenseProfilerComponent extends AnytimeComponent {

    double timeLimit = 0;
    
    public AnytimeSenseProfilerComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
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
    public void wakeUp(boolean ts) {
        synchronized (aag.objSense) {
            if (ts || sleeping) {
                sleeping = false;
                enqueueExecutor(ts);
            }
        }
    }*/

    public void enqueueExecutor(boolean ts) {
//        if (!inQueue || ts) {
//            inQueue = true;
//            aag.getExecutorSense().execute(this);
//        }
    }

    public void run() {
//        System.out.println("anytime sense");
//        int cycles = aag.getCyclesSense();
//        //int number_cycles = 1;
//        int i = 0;
//
//        while (aag.isRunning() && i < cycles) {
//            i++;
            aag.getTS().anytimeSenseProfiler();
//        aag.getTS().sense();
//
//            synchronized (aag.objSense) {
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
