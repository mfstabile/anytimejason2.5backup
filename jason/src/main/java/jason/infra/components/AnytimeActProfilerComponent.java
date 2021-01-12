package jason.infra.components;

import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;

public class AnytimeActProfilerComponent extends AnytimeComponent {

    double timeLimit = 0;
    
    public AnytimeActProfilerComponent(CentralisedAgArchAnytimeAsynchronous centralisedAgArchAsynchronous) {
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
////        System.out.println("anytime run");
//        int cycles = aag.getCyclesAct();
//        //int number_cycles = 1;
//        int i = 0;
//        while (aag.isRunning() && i < cycles) {
////            System.out.println(i+" - "+ cycles);
//            i++;
            aag.getTS().anytimeActProfiler();
//            aag.getTS().act();
//
//
//            synchronized (aag.objAct) {
//                if (canSleep()) {
//                    inQueue = false;
//                    sleep();
//                    return;
//                } else if (i == cycles) {
////                    enqueueExecutor(true);
//                    return;
//                }
//            }
//        }
    }
}
