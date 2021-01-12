package jason.infra.centralised;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.math3.random.EmpiricalDistribution;

public class EmpiricalDistributionsProfiler implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7488457720424585010L;

    private HashMap<String, ArrayList<Double>> arrayMap = new HashMap<String, ArrayList<Double>>(20);
    
    protected static Logger logger  = Logger.getLogger(CentralisedAgArch.class.getName());
        
    public void addTime(String s,double time) {
        ArrayList<Double> ar = arrayMap.get(s);
        if (ar==null) {
            ar = new ArrayList<Double>();
            arrayMap.put(s, ar);
        }
        ar.add(time);
    }
    
    public boolean save(String agName) {
        EmpiricalDistributionsController edc = new EmpiricalDistributionsController();
        
//        File file = new File("profiling/"+agName+"-log.txt");
//        FileWriter fr = null;
//        try {
//            fr = new FileWriter(file, true);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        for (HashMap.Entry<String, ArrayList<Double>> entry : arrayMap.entrySet()) {

            ArrayList<Double> al = entry.getValue();
            
            double[] d = new double[al.size()];
                
            for (int i = 0; i < d.length; i++) {
                d[i] = al.get(i);
            }
            
            EmpiricalDistribution ed = new EmpiricalDistribution(Math.min(1000, d.length));
            ed.load(d);
            
            edc.addDistribution(entry.getKey(), ed);
            
//            try {
//                fr.write("\n"+entry.getKey() + ":" + al.size());
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            
        }
//        try {
//            fr.write("\n-------------------------------------------");
//            fr.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        if (!EmpiricalDistributionsController.saveObject(edc, agName)) {
            System.out.println("Error saving file");
        }
        
        return true;
    }
}
    
