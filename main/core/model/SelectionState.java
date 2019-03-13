/**     
 *  =========================================================== 
 *  author :  Yari Yousefian
 *  This class is about current selection state of the system  
 *  =========================================================== 
 */

import java.util.ArrayList ;

public class SelectionState extends State
{
	
	private ArrayList<Range> ranges;
	
	
	SelectionState()
    {   	
		off();
		ranges = new ArrayList<Range>(7);
    }
    
   public void add(int low, int high)
   {
	  ranges.add(new Range(low,high)); 
   }
   
   public boolean selected(int index)
   {	  
	   boolean result = false;
	   for(Range r : ranges)
	   {
		 if( (index>=r.lower()) && (index<=r.upper()) ) { result = true; break; }
	   }
	   System.out.println(index +" in ? "+result);
	   return result;
   }
   
    public void reset()
    {   	
    	ranges.clear();
    	off(); 
    }
   
    
    
   private class Range
   {
   
       private int upperBound; 
       private int lowerBound;

       Range(int low, int up)
       {
       	lowerBound = low;
       	upperBound = up;
       }
       
       public int lower()
       {
    	   return lowerBound;
       }
      
       public int upper()
       {
    	   return upperBound;
       }
     
  }
   
}
