package jason.bb;

import java.util.Iterator;
import java.util.LinkedList;

import jason.asSyntax.Literal;

public class PriorizedPerceptionList implements Iterable<Literal>{
    
    private int maxSections = 5;
    
    private int size = 0;
    
    private LinkedList<Literal>[] structure = new LinkedList[maxSections];
    
    public PriorizedPerceptionList(Iterator<Literal> percepts, String priorityMechanism) {
        for(int i = 0; i < maxSections; i++) {
            structure[i] = new LinkedList<Literal>();
        }
        
        switch (priorityMechanism) {        
        case "annotationSort":
        default:
            annotationSort(percepts);
            break;
        }
    }
    
    private void annotationSort(Iterator<Literal> percepts) {
        while(percepts.hasNext()) {
            Literal nextPercept = percepts.next();
            
            try {
                structure[Integer.parseInt(nextPercept.getAnnot("priority").getTerm(0).toString())].add(0, nextPercept);
            } catch (Exception e) {
                structure[0].add(0, nextPercept);
            }
            
            size = getSize() + 1;
        }
    }

    @Override
    public Iterator<Literal> iterator() {
        // TODO Auto-generated method stub
        return new PriorizedPerceptionListIterator(this);
    }

    
    
    public int getSize() {
        return size;
    }



    class PriorizedPerceptionListIterator implements Iterator<Literal> {
        
        private int iterated = 0;
        
        private int currentList = 0;
        
        Iterator<Literal> iter;
          
        // constructor 
        PriorizedPerceptionListIterator (PriorizedPerceptionList obj) { 
            iter = structure[0].iterator();
        } 
          
        // Checks if the next element exists 
        public boolean hasNext() {
            return iterated<getSize();
        } 
          
        // moves the cursor/iterator to next element 
        public Literal next() {
            if(iter.hasNext()) {
                iterated++;
                return iter.next();
            }
            
            while(currentList<maxSections-1) {
                currentList++;
                iter = structure[currentList].iterator();
                if(iter.hasNext()) {
                    iterated++;
                    return iter.next();
                }
            }
            return null; 
        } 
          
        // Used to remove an element. Implement only if needed 
        public void remove() { 
            throw new UnsupportedOperationException();
        } 
    } 
    
}
