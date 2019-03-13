/**     
 *  ============================================================== 
 *  author :  Yari Yousefian
 *  This class - what kind of state/type current expression is in  
 *  ============================================================== 
 */



public class ExpressionState extends State
{
	
	private int currentMode;  //  expand = 0 , compress = 1, off = -1
	
	
	ExpressionState()
    {   	
		on = false;
		currentMode = -1;
    }
    
  
   
   public int currentMode()
   { 
     return currentMode ;
   }
   
    
    public void change(int mode)
    {   		
    	if(active())currentMode = mode;
    }
   
    @Override public void on()
    {
    	on = true;
    	currentMode = 0;
    }  
}