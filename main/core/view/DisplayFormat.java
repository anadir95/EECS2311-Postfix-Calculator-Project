/**     
 *  =============================================================================== 
 *  author :  Yari Yousefian
 *  This class format the result to be shown. Shows fraction part in 2 decimal place 
 *  and does not show the fraction part if the fraction part is zero  
 *  ============================================================================== 
 */

import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class DisplayFormat
{
	   private Double value ;
	   private boolean fragmented, scientific ;
	   private String result ;
	   private String integerPart, fraction;
	  
	   public DisplayFormat(Double val)
	   {
		   value = val;
		   scientific = false;
	       process();
	      
	   }
	   
	   public String format()
	   {		   	       
	       return result;
	   }
	   
	   public void reset(Double val)
	   {
	      value = val;
	      scientific = false;
	      process();
	   }
	   private void process()
	   {
		   fragmented = true;
		   DecimalFormat df = new DecimalFormat("#.##");
		  
	        Double doubleValue =  Double.valueOf(df.format(value));
	         result = preventScientific( Double.toString(doubleValue) ) ;
		   
	        
		   
	       
		  if(!scientific)
		  {
	       StringTokenizer tokens = new StringTokenizer(result,".");
	       
	       integerPart = tokens.nextToken();
	       fraction = tokens.nextToken();
	       int fractionValue = Integer.parseInt(fraction); 
	       if(fractionValue == 0)  // eg: 25.00
	       { 
	    	   result = integerPart;  // if 25.00 then make it 25
	    	   fragmented = false; 
	       }
	       else if(fractionValue<10) result += "0" ; // show in 2 decimal place
		  } 
		   
	   }
	   public boolean hasFraction()
	   {
		   
	       return fragmented;
	   }
	   public String getFraction()
	   {
		   
	       return fraction;
	   }
	   public String getIntegerPart()
	   {
		   
	       return integerPart;
	   }
	   
	   private String preventScientific(String s)  // detect scientific notation and convert
	   {
		   if(s.indexOf('E')<0)  return s;
		   
		   scientific = true;
		   StringTokenizer tokens = new StringTokenizer(s,"E"); 
		   String number = tokens.nextToken();
		   tokens = new StringTokenizer(number,".");
		   String first = tokens.nextToken();
		   String second = tokens.nextToken();
		   
	       return first+second ;
	   }
	   
}