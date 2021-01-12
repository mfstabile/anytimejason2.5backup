package jason.infra.components;

import java.util.logging.Logger;

import jason.asSemantics.CircumstanceListener;
import jason.asSemantics.Event;
import jason.asSemantics.Intention;
import jason.infra.centralised.CentralisedAgArchAnytimeAsynchronous;
import jason.infra.centralised.CentralisedAgArchAsynchronous;

public class CircumstanceListenerComponents implements CircumstanceListener {
    private CentralisedAgArchAsynchronous ag;
    private CentralisedAgArchAnytimeAsynchronous aag;

    public CircumstanceListenerComponents(CentralisedAgArchAsynchronous ag) {
        this.ag = ag;
    }
    
    public CircumstanceListenerComponents(CentralisedAgArchAnytimeAsynchronous aag) {
        this.aag = aag;
    }

    public void notifyDeliberate() {
        if (ag != null){
            ag.wakeUpDeliberate();
        } else{
            aag.wakeUpDeliberate();
        }
        
    }

    public void notifyAct() {
        if (ag != null){
            ag.wakeUpAct();
        } else{
            aag.wakeUpAct();
        }
    }

    public void eventAdded(Event e) {
//        Logger LOG = Logger.getLogger(ag.getAgName());
//        LOG.info("eventAdded");
        notifyDeliberate();
    }

    public void intentionAdded(Intention i) {
//        Logger LOG = Logger.getLogger(ag.getAgName());
//        LOG.info("intentionAdded");
        notifyAct();
    }

    public void intentionDropped(Intention i) {
//        Logger LOG = Logger.getLogger(ag.getAgName());
//        LOG.info("intentionDropped");
        notifyDeliberate();
    }

    public void intentionSuspended(Intention i, String reason) {
//        Logger LOG = Logger.getLogger(ag.getAgName());
//        LOG.info("intentionSuspended");
        notifyDeliberate();
    }

    public void intentionResumed(Intention i) {
        //notifyDeliberate();
//        Logger LOG = Logger.getLogger(ag.getAgName());
//        LOG.info("intentionResumed");
        notifyAct();
        
    }
}
