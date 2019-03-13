/**     
 *  ==================================================================== 
 *  author :  Yari Yousefian
 *  represents different states of the system at any point of execution  
 *  ==================================================================== 
 */


public class State
{
		protected boolean on;
		
		
		 public void toggle()
		 {
		     if(on)  	on = false;  
		     else on = true;
		 }
		 
		 public boolean active()
		 {
		       return on;	
		 }
		 public void on()
		 {
		       on = true;	
		 }
		 public void off()
		 {
		       on = false;	
		 }	
		    
}