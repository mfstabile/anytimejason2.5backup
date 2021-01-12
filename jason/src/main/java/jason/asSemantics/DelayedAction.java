package jason.asSemantics;

public class DelayedAction implements Comparable<DelayedAction>{
    public ActionExec ac;
    public Intention i;
    
    public DelayedAction(ActionExec ac, Intention i) {
        this.ac = ac;
        this.i = i;
    }
    
    @Override
    public int compareTo(DelayedAction o) {
        return ac.compareTo(o.ac);
    }
}
