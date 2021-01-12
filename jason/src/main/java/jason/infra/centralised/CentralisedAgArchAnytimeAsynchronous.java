package jason.infra.centralised;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import jason.JasonException;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Circumstance;
import jason.asSemantics.CircumstanceListener;
import jason.asSemantics.DelayedAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.infra.components.AnytimeActComponent;
import jason.infra.components.AnytimeActProfilerComponent;
import jason.infra.components.AnytimeComponent;
import jason.infra.components.AnytimeDeliberateComponent;
import jason.infra.components.AnytimeDeliberateProfilerComponent;
import jason.infra.components.AnytimeSenseComponent;
import jason.infra.components.AnytimeSenseProfilerComponent;
import jason.infra.components.CircumstanceListenerComponents;

public class CentralisedAgArchAnytimeAsynchronous extends CentralisedAgArch implements Runnable {
    private AnytimeComponent senseComponent;
    private AnytimeComponent deliberateComponent;
    private AnytimeComponent actComponent;

//    private ExecutorService executorSense;
//    private ExecutorService executorDeliberate;
//    private ExecutorService executorAct;
    private ExecutorService executor;

    public Object objSense = new Object();
    public Object objDeliberate = new Object();
    public Object objAct = new Object();

    private Future<?> fAct;
    private Future<?> fDel;
    private Future<?> fSen;

    private long responseTime;
    private long endTime;
    private String beliefPriority = "";

    private boolean isProfilingv;
    
    private boolean debug = false;
    
    private EmpiricalDistributionsController edc = null;
    
    private EmpiricalDistributionsProfiler edp;

    private ActionExec defaultAction;
    
    private double executionTime;
    
    public final static Object obj = new Object();

    public CentralisedAgArchAnytimeAsynchronous(String agName, boolean debug) {
        super();
//        executorSense = Executors.newSingleThreadExecutor();
//        executorDeliberate = Executors.newSingleThreadExecutor();
//        executorAct = Executors.newSingleThreadExecutor();

        executor = Executors.newFixedThreadPool(3);
        
        edc = EmpiricalDistributionsController.loadObject(agName);
        this.debug = debug;
        
        if (edc!=null) {
            executionTime = edc.getExecutionTime("executeAction");
            setProfiling(false);
            senseComponent = new AnytimeSenseComponent(this);
            deliberateComponent = new AnytimeDeliberateComponent(this);
            actComponent = new AnytimeActComponent(this);
        } else {
            edp = new EmpiricalDistributionsProfiler();
            // profile
            setProfiling(true);
            senseComponent = new AnytimeSenseProfilerComponent(this);
            deliberateComponent = new AnytimeDeliberateProfilerComponent(this);
            actComponent = new AnytimeActProfilerComponent(this);
        }
    }

    public void run() {
        if(!isProfiling()) {
            executor.submit(actComponent);
            executor.submit(deliberateComponent);
            executor.submit(senseComponent);
        }
        // ?
        addListenerToC(new CircumstanceListenerComponents(this));
        TransitionSystem ts = getTS();
        while (this.isRunning()) {
            Logger LOG = Logger.getLogger(getAgName());
            if (ts.getSettings().isSync()) {
                this.waitSyncSignal();
                if(!isProfiling()) {
                    endTime = (long) (System.nanoTime() + responseTime - executionTime);
                    edc.setTimeLimit(endTime);
                    edc.setTime(true);
                    LOG.info("time remaining:"+ (responseTime - executionTime));
                }
                monitorCicle();
                boolean isBreakPoint = false;
                try {
                    isBreakPoint = ts.getC().getSelectedOption().getPlan().hasBreakpoint();
                    if (logger.isLoggable(Level.FINE))
                        logger.fine("Informing controller that I finished a reasoning cycle " + getCycleNumber()
                                + ". Breakpoint is " + isBreakPoint);
                } catch (NullPointerException e) {
                    // no problem, there is no sel opt, no plan ....
                }
                informCycleFinished(isBreakPoint, getCycleNumber());
            } else {
                getUserAgArch().incCycleNumber();
                if(!isProfiling()) {
                    endTime = (long) (System.nanoTime() + responseTime - executionTime);
                    edc.setTimeLimit(endTime);
                    edc.setTime(true);
                    LOG.info("time remaining:"+ (responseTime - executionTime));
                }
                monitorCicle();
//              if (ts.canSleep())
//                  sleep();
            }
        }
    }

    private void monitorCicle() {
        // Dispara a execução pelo tempo determinado
        try {
            if(isProfiling()) {
//                fAct = executorAct.submit(actComponent);
//                fDel = executorDeliberate.submit(deliberateComponent);
//                fSen = executorSense.submit(senseComponent);
//                
                fAct = executor.submit(actComponent);
                fDel = executor.submit(deliberateComponent);
                fSen = executor.submit(senseComponent);
                
                try {
                    fSen.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    fDel.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    fAct.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long begin = System.nanoTime();
                if(executeAction()){
                    long time = System.nanoTime() - begin;
                    edp.addTime("executeAction", time);
                }
                if(getCycleNumber()%1000==0 && getCycleNumber()>0) {
                    edp.save(getAgName());
                }
            }else {

                long wait = endTime - System.nanoTime();
                if(wait>0) {
                    long waitMilis = wait / 1000000;
                    int waitNano = (int) (wait % 1000000);
                    synchronized (obj) {
                        obj.wait(waitMilis,waitNano);
                    }
                }
                edc.setTime(false);
                executeAction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean executeAction() {
        Circumstance C = getTS().getC();
        DelayedAction da = C.getAPQ().poll();
        if (da != null) {
            ActionExec action = da.ac;
            
            C.addPendingAction(action);
            // We need to send a wrapper for FA to the user so that add method then calls
            act(action); // , C.getFeedbackActionsWrapper());
            try {
                getTS().applyClrInt(da.i);
            } catch (JasonException e) {
                logger.log(Level.SEVERE, "*** ERROR in the transition system (act). Unknown behavior ahead", e);
                e.printStackTrace();
            }            
        }else {
            //execute default action
//            System.out.println("Default action: "+defaultAction);
//            C.addPendingAction(defaultAction);//?
//            act(defaultAction);
//            return false;
        }
        return true;
    }
    
    public void setDefaultAction(ActionExec defaultAction) {
        this.defaultAction = defaultAction;
    }

    public void wakeUpSense() {
        if(!debug) {
            System.out.println(getAgName() + " Receiving signal");
            if(!isProfiling())receiveSyncSignal();
        }
        senseComponent.wakeUp();
    }

    public void wakeUpDeliberate() {
        deliberateComponent.wakeUp();
    }

    public void wakeUpAct() {
        actComponent.wakeUp();
    }

    public AnytimeComponent getSenseComponent() {
        return senseComponent;
    }

    public AnytimeComponent getDeliberateComponent() {
        return deliberateComponent;
    }

    public AnytimeComponent getActComponent() {
        return actComponent;
    }

    public void setSenseComponent(AnytimeSenseComponent senseComponent) {
        this.senseComponent = senseComponent;
    }

    public void addListenerToC(CircumstanceListener listener) {
        getTS().getC().addEventListener(listener);
    }

    public void receiveMsg(Message m) {
        synchronized (objSense) {
            mbox.offer(m);
        }
    }

    /** called the the environment when the action was executed */
    public void actionExecuted(ActionExec action) {
        synchronized (objAct) {
            super.actionExecuted(action);
        }
    }

    public long getEndTime() {
        return endTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getBeliefPriority() {
        return beliefPriority;
    }

    public void setBeliefPriority(String beliefPriority) {
        this.beliefPriority = beliefPriority;
    }

    public EmpiricalDistributionsController getEDC() {
        return edc;
    }

    public boolean isProfiling() {
        return this.isProfilingv;
    }

    public void setProfiling(boolean isProfiling) {
        this.isProfilingv = isProfiling;
    }
    
    public EmpiricalDistributionsProfiler getEDP() {
        return edp;
    }
}
