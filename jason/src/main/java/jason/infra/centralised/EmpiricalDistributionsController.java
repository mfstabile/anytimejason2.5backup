package jason.infra.centralised;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.commons.math3.random.EmpiricalDistribution;

public class EmpiricalDistributionsController implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7488457720424585010L;

    private HashMap<String, EmpiricalDistribution> distributionsMap = new HashMap<String, EmpiricalDistribution>(20);
    private HashMap<String, Double> timeMap = new HashMap<String, Double>(20);
    
    protected static Logger         logger  = Logger.getLogger(CentralisedAgArch.class.getName());
    
    private double confidenceLimit = 1;
    
    private long timeLimit = 0;
    
    private boolean time = false;
    
    public EmpiricalDistributionsController() {
    }
    
    public void addDistribution(String s, EmpiricalDistribution e) {
        distributionsMap.put(s, e);
        timeMap.put(s, e.inverseCumulativeProbability(confidenceLimit));
    }
    
    
    public double getExecutionTime(String s) {
        return timeMap.getOrDefault(s, .0);
    }
    
    public boolean setLimit(double newLimit) {
        if (newLimit >= 0 && newLimit <= 100) {
            confidenceLimit = newLimit;
            
            for (HashMap.Entry<String, EmpiricalDistribution> entry : distributionsMap.entrySet()) {
                double newVal = entry.getValue().inverseCumulativeProbability(newLimit);
                timeMap.put(entry.getKey().toString(), newVal);
            }
            return true;
        }
        return false;
    }
    
    public boolean hasTime(String s) {
        if(timeLimit - timeMap.getOrDefault(s, .0) > System.nanoTime()) {
            return true;
        }
        return false;
    }
    
    public static boolean saveObject(EmpiricalDistributionsController d, String s) {
        // Write to disk with FileOutputStream
        try {
            FileOutputStream f_out = new FileOutputStream("profiling/"+s+".tempdata");
    
            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
    
            // Write object out to disk
            obj_out.writeObject ( d );
            
            obj_out.close();
            
            File f1 = new File("profiling/"+s+".data");
            f1.delete();
            
            File f2 = new File("profiling/"+s+".tempdata");
            f2.renameTo(f1);
            
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    
    public boolean saveObject(String s) {
        return saveObject(this, s);
    }
    
    public static EmpiricalDistributionsController loadObject(String s) {
        
        try {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream("profiling/"+s+".data");
            
            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream (f_in);

            // Read an object
            Object obj = obj_in.readObject();

            obj_in.close();
            
            EmpiricalDistributionsController comp = (EmpiricalDistributionsController) obj;
            return comp;

        }catch (FileNotFoundException e) {
            return null;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.log(Level.SEVERE, "Error reading the anytime profiling files. Changing to profile mode.");
        }
        return null;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean hasTime() {
        return time;
    }

    public void setTime(boolean time) {
        this.time = time;
    }
}
