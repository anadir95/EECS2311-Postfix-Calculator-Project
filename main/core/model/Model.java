/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class is model, the actual calculator   
 *  =================================================== 
 */

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Model  
{
	private final double PI = 3.14159 ;
	private final int OPRAND = 1;
	private final int COMPUTED = 2;
	private final int COMPOUND = 3;
	private final int EXPAND = 0;
	private final int COMPRESS = 1;
	
	
	private Stack<Action<Double>> stack;
	private int  totalComputations;
	private DisplayFormat format;
	private SelectionState selection;
	private int totalOprands;
	private int inserted = 0;
	private Expression<Double> expressionType;  // postfix or infix or prefix 
	private ExpressionState expressionMode;
	private Stack<Action<Double>> deleted;
	
	
	/**
	 * Creates a model with no user values and a calculated
	 * value of zero.
	 * 
	 */
	public Model(Expression<Double> expr)
	{
		stack = new  Stack<Action<Double>>();
		format = new DisplayFormat(0.00);
		selection = new SelectionState() ;
		deleted = new  Stack<Action<Double>>();
		 expressionType = expr;  // infix , postfix etc
		 expressionMode = new ExpressionState();
		 totalOprands = 0;
		 totalComputations = 0;
		 inserted = 0;
		 
	}

	public void insert(Double entry)
	{ 
		format.reset(entry);
		String str = format.format();
		
		Action<Double> newEntry = new Action<Double>(entry,str,5,++totalOprands) ;
		if(!deleted.empty()) newEntry.setPrevious(invert(deleted));  // invert is used here so that later its easier to push back in 
			
		stack.push( newEntry );
		expressionMode.off();
		inserted++;
	}
	
	public String remove()  //  undo
	{
          if(size()==1)stack.pop();
          else
          {
		    
		   if((inserted==0) && !(deleted.empty())) {stack.push(deleted.pop());totalOprands++;return "removed";}
		    if(size()>0) 
		    {
			   if (!selection.active())    // delete last item on stack
				{
					Action<Double> top =  stack.pop();
					if(top.type()==OPRAND)totalOprands--;
					else totalComputations--;
					if(top.type()>1)  // not oprand
					{
						if(top.type()==COMPOUND)	
						{
							Action<Double> right =	top.remove();
					    	right.setLeftOperator(0);
							top.setValue(Double.parseDouble(top.getLocalValue()));
							
							
							 if(top.type()==OPRAND)top.setPosition(stack.peek().position()+1);
							 stack.push(top);
							 if(right.position()!=-111)stack.push(right);
							
						}
						else if(top.type()==COMPUTED)  
						{
							String[] trv = top.getOprands();
							int previous ; // position of last oprand at last state
							if(stack.peek().position()==0)previous = 0;
							else previous = stack.peek().position();
							for(int i = 0; i < trv.length; i++)stack.push(new Action<Double>(Double.parseDouble(trv[i]),trv[i],5,previous+i));
							deleted.push(top);
						}
						
					}
					else  // oprand type
					{
						Stack<Action<Double>> previous = top.getPrevious();
						while(!previous.empty()) stack.push(previous.pop());
						deleted.push(top);  // think
						
					}
					
				}
				else if(totalOprands>0)  // delete selected from current
				   {
					  Stack<Action<Double>> selected = pull(span());
					  while( !selected.empty() ) selected.pop() ; // garbage collection	
				   }
			 
		    }
		    else return "nothing to undo !"; 
          }
		    selection.reset();
		    
		    inserted--;
		    return expression(EXPAND);
					
	}
	
	
	/**
	 * Clears the user values and the calculated value.
	 */
	public void purge()
	{
		while(!stack.empty())stack.pop() ;
		
		
		selection.reset();
		totalOprands = 0;
		totalComputations = 0;
		expressionMode.off();
	}

	
	public void add()
	{		
		Stack<Double> selected, tmp  ;
		Action<Double> result ;  // result item to be pushed on stack
		Double sum = 0.00;
		
		 if( selection.active() ) // add selected
		 { 
			 System.out.println("span = "+span());
			 selected = pick(span()); 
			 tmp = new Stack<Double>();
			 
			 while( !selected.empty() )
			 { sum +=  selected.peek() ;  tmp.push(selected.pop()) ; }
			 
			 format.reset(sum);
			 String formattedSum = format.format() ;
			 result = new Action<Double>(sum, formattedSum ,5,++totalOprands) ; // new oprand to be added
			 
			  
			 stack.push(result);
			 selection.reset();		 
		 }
		 else   //  add last 2 
			{
			 
			    Action<Double> top = stack.pop();
			    sum += top.getValue() ;
			    if(stack.empty()) // had only 1 item so add it with 0
			    {	
		           result = new Action<Double>(0.0,"0",5,-111);  
		 		  
		 		   result.merge(top,1,sum);
		 		 
			    }	
			    else 
			    {
			    	
			    	      result = stack.pop();
				    	  sum += result.getValue() ;
				    	  
				    	  if(top.type()==OPRAND && result.type()==OPRAND)  // add only oprands
			    	      {
				    		  // System.out.println("here2ya");
			    	    	   String[] tp = top.getOprands();
			    	    	   String[] rs = result.getOprands();
			    	    	   int size1 = rs.length ;
			    	    	   int size2 = tp.length ;
			    	    	   String[] opr = new String[size1+size2];
			    	    	   for(int i = 0; i < size1; i++) opr[i] = rs[i];
			    	    	   for(int i = 0; i < size2; i++) opr[i+size1] = tp[i];
			    	    	   format.reset(sum);
			    			   
			    	    	   result = new Action<Double>(sum,format.format(),1,0);
			    	    	   result.setOprands(opr);
			    	      }
				    	  else  result.merge(top,1,sum); 
			    	      
			    	      
			    }
			    stack.push(result);
	    	    totalComputations++;
	    	    totalOprands = 0; 
			    	    	
			}
		 expressionMode.off();
		 
	}
	
	/**
	 * Subtracts the calculated value by a user value.
	 * 
	 * @param userValue
	 *            The value to subtract from the current calculated value by.
	 */
	public void subtract()
	{
		
		Stack<Double> selected, tmp  ;
		Action<Double> result ;  // result item to be pushed on stack
		Double sub;
		
		 if( selection.active() ) // add selected
		 { 
		     selected = pick(span()); 
			 tmp = new Stack<Double>();
			 tmp.push(selected.pop()) ;
			 sub = tmp.peek();
			 
			 while( !selected.empty() ){ sub -=  selected.peek() ;  tmp.push(selected.pop()) ; }
			 format.reset(sub);
			 result = new Action<Double>(sub,format.format(),5,++totalOprands) ; 
			 
			 String[] opr = new String[tmp.size()];
			 while(!tmp.empty())
			 {
			   format.reset(tmp.pop());
			   opr[tmp.size()] = format.format();
			 }
			 result.setOprands(opr);
			 
			 
			 stack.push(result);
			 selection.reset();		 
		 }
		 else   //  last 2 
			{
			
			    Action<Double> top = stack.pop();
			    
			    if(stack.empty()) // had only 1 item so subtract  it from 0
			    {	
		           result = new Action<Double>(0.0,"0",5,-111);  //test
		 		   
		 		   result.merge(top,2,0-top.getValue());
		 		   
			    }	
			    else 	 // empty at this point would mean add to zero 
			    {
			    	
			    	      result = stack.pop();
				    	  sub = result.getValue() - top.getValue() ;
			    	      
				    	  if(top.type()==OPRAND && result.type()==OPRAND)  // add only oprands
			    	      {
			    	    	   String[] tp = top.getOprands();
			    	    	   String[] rs = result.getOprands();
			    	    	   int size1 = rs.length ;
			    	    	   int size2 = tp.length ;
			    	    	   String[] opr = new String[size1+size2];
			    	    	   for(int i = 0; i < size1; i++) opr[i] = rs[i];
			    	    	   for(int i = 0; i < size2; i++) opr[i+size1] = tp[i];
			    	    	   format.reset(sub);
			    			   
			    	    	   result = new Action<Double>(sub,format.format(),2,0);
			    	    	   result.setOprands(opr);
			    	      }
				    	  else result.merge(top,2,sub);  
				        
			    }
			    
			    stack.push(result);
	    	    totalComputations++;
	    	    totalOprands = 0;  	
			    	    	
			}
		 expressionMode.off();
		
		
		
		
		
		
	}
	
	/**
	 * Multiplies the calculated value by a user value.
	 * 
	 * @param userValue
	 *            The value to multiply the current calculated value by.
	 */
	public void multiply()
	{
		Stack<Double> selected, tmp  ;
		Action<Double> result ;  // result item to be pushed on stack
		Double mult = 1.00;
		
		 if( selection.active() ) // add selected
		 { 
			 selected = pick(span()); 
			 tmp = new Stack<Double>();
			 //System.out.println("here1ya");
			 while( !selected.empty() )
			 { mult *=  selected.peek() ;  tmp.push(selected.pop()) ; }
			 
			 format.reset(mult);
			 String formattedMult = format.format() ;
			 result = new Action<Double>(mult, formattedMult ,5,++totalOprands) ; // new oprand to be added
			 
			 
			 stack.push(result);
			 selection.reset();		 
		 }
		 else   //  add last 2 
			{
			   
			    Action<Double> top = stack.pop();
			    mult *= top.getValue() ;
			    
			    if(stack.empty()) // had only 1 item so add it with 0
			    {	
		           result = new Action<Double>(1.0,"1",5,-111);  //test
		 		   
		 		   result.merge(top,3,mult);
		 		 
			    }	
			    else // empty at this point would mean add to zero 
			    {
			    	
			    	      result = stack.pop();
				    	  mult *= result.getValue() ;
				    	  if(top.type()==OPRAND && result.type()==OPRAND)  // add only oprands
			    	      {
			    	    	   String[] tp = top.getOprands();
			    	    	   String[] rs = result.getOprands();
			    	    	   int size1 = rs.length ;
			    	    	   int size2 = tp.length ;
			    	    	   String[] opr = new String[size1+size2];
			    	    	   for(int i = 0; i < size1; i++) opr[i] = rs[i];
			    	    	   for(int i = 0; i < size2; i++) opr[i+size1] = tp[i];
			    	    	   format.reset(mult);
			    			   
			    	    	   result = new Action<Double>(mult,format.format(),3,0);
			    	    	   result.setOprands(opr);
			    	      }
				    	  else result.merge(top,3,mult);  
				    	  
				    	 
			    	      
			    	      
			    }
			    stack.push(result);
	    	      totalComputations++;
	    	      totalOprands = 0;
			    	    	
			}
		 expressionMode.off();
		
		
	}
	
	/**
	 * Divides the calculated value by a user value.
	 * 
	 * @param userValue
	 *            The value to multiply the current calculated value by.
	 * @pre. userValue is not equivalent to zero.
	 */
	public String divide()
	{
		Stack<Double> selected, tmp  ;
		Action<Double> result ;  // result item to be pushed on stack
		Double divident,divisor;
		
		
		
		 if( selection.active() ) // add selected
		 { 
			 
            selected = pick(span()); 
			 
			 tmp = new Stack<Double>();
			 tmp.push(selected.pop()) ;
			 divident = tmp.peek();
			 
			 while( !selected.empty() )
		     {    
				   divisor = selected.peek() ; tmp.push( selected.pop() ) ;
				   if (divisor == 0.00) return "oops, division by zero !"; 
				   else divident /= divisor; 
			 }
			 		 
			 format.reset(divident);
			 result = new Action<Double>(divident,format.format(),5,++totalOprands) ; 
			 
			 String[] opr = new String[tmp.size()];
			 while(!tmp.empty())
			 {
			   format.reset(tmp.pop());
			   opr[tmp.size()] = format.format();
			 }
			 result.setOprands(opr);
			 stack.push(result);
			 selection.reset();	
				 
		 }
		 else   //  add last 2 
			{

			    Action<Double> top = stack.pop();
			    divisor = top.getValue() ;
			    if(divisor==0)return "oops, division by zero !";
			    if(stack.empty()) // had only one element b4 pop up
			    {
			    	   result = new Action<Double>(0.0,"0",5,-111);
			 		   
			 		   result.merge(top,4,0.00);  
			 		   stack.push(result);
			 		   totalComputations++;
		    	       totalOprands = 0;
			    }
			    else 
			    {
		
			    	result = stack.pop();
			    	divident = result.getValue() / divisor ;
			    	
			    	if(top.type()==OPRAND && result.type()==OPRAND)  // add only oprands
		    	      {
		    	    	   String[] tp = top.getOprands();
		    	    	   String[] rs = result.getOprands();
		    	    	   int size1 = rs.length ;
		    	    	   int size2 = tp.length ;
		    	    	   String[] opr = new String[size1+size2];
		    	    	   for(int i = 0; i < size1; i++) opr[i] = rs[i];
		    	    	   for(int i = 0; i < size2; i++) opr[i+size1] = tp[i];
		    	    	   format.reset(divident);
		    			   
		    	    	   result = new Action<Double>(divident,format.format(),4,0);
		    	    	   result.setOprands(opr);
		    	      }
			    	  else result.merge(top,4,divident);    
			    	
			    	
		    	      stack.push(result);
		    	      totalComputations++;
		    	      totalOprands = 0;
			    }
			   	
			}
		 expressionMode.off();
		
		return "done";
		 
		
	}
	
	public double sin(int degree)
	{
		expressionMode.off();
		return Math.sin(radian(degree)) ;
		 
	}
	
	public double cos(int degree)
	{
		expressionMode.off();
		return Math.cos(radian(degree)) ;
		
	}
	
	public int factorial(int n) 
	{
		expressionMode.off();
	    return (n <= 1) ? 1 : n*factorial(n-1);
	}
	
	public double radian(int deg)
    {
		expressionMode.off();
		return ((2*Math.PI)/360.0) * deg;
    }
	
	

	public void select(String inp)
	{		
		 if(occurance(inp,".")==0)
		    {
		    	int index = Integer.parseInt(inp);
		    	selection.add(index,index);
		    }
		    else
		    {	       
		       StringTokenizer tokens = new StringTokenizer(inp,".");
			   String lower = tokens.nextToken();
			   String higher = tokens.nextToken();
			   selection.add(Integer.parseInt(lower),Integer.parseInt(higher));		 
		    }	    
		selection.on();
	    expressionMode.off();
    }
	

	private Stack<Action<Double>> pull(int range)  // extract desired elements from stack,an item is desired either if its selected or its in specified range
	{
		
		Stack<Action<Double>> result = new  Stack<Action<Double>>(); // selected
		Stack<Action<Double>> tmp = new  Stack<Action<Double>>(); 
		
		
		while(range>0)
		{
			if(!selection.active())result.push(stack.pop());
			else if( selection.selected( stack.peek().position() ) )
				{
				  result.push(stack.pop());
				  if(result.peek().position()==0) totalComputations--;
				}
			else tmp.push(stack.pop());
			range--;
		}
		totalOprands = 0;
		while(!tmp.empty())
		{
			tmp.peek().setPosition(++totalOprands);  // update index of existing oprands
			stack.push(tmp.pop());
		}
			
		return result;
		
	}  
	
	
	
	 private Stack<Double> pick(int range)  // dont extract but return the values of desired items in stack , an item is desired either if its selected or its in specified range
		{
		
		 Stack<Double> result = new Stack<Double>();
	     Stack<Action<Double>> tmp = new  Stack<Action<Double>>(); 
	     
		 boolean current = (totalOprands>0);
	     
	     if(selection.active())  // pick from selected items
		 {
			 while(range>0)
			   { 	
				 System.out.println("range = "+range);
				   tmp.push(stack.pop());
				       
					     if(current)  // from oprands
				         {	    	   
					       if(selection.selected( range )) result.push(tmp.peek().getValue());
				    	   range--;
				         } 
					     else if(  tmp.peek().type()>1  )    // dont add if its from history but this item not computed
					       {
					    	   if(selection.selected( range ))result.push(tmp.peek().getValue());
					           range--;
					       }			  	    		    
	    	   }
		 }
		 else  // pick last x items such that x = range
		 {
			 while(range>0)
			 { 		    		    
				   tmp.push(stack.pop());
				   
				   if(current)
				   {
					   result.push(tmp.peek().getValue());
				       range--;   
				   }
				   else if(tmp.peek().type()>1)   // from history
					   {
					       result.push(tmp.peek().getValue());
					       range--;
					   }
	    	 } 
		 }
		 
		 while(!tmp.empty()) stack.push(tmp.pop());
		 return result; 
		 	
		}
	  
	 
	 public String negate()  // can negate only from current oprands 
	 {
		    
			 if(totalOprands<1) return "May not modify history !" ;
			 
			if(selection.active())
			{
			  Stack<Action<Double>> tmp = new  Stack<Action<Double>>(); 
			  int range = totalOprands;	
				
				while(range>0)
				{
					tmp.push(stack.pop());
					if( selection.selected( tmp.peek().position()  ) )
						{
						   Double v = -1.00 ;
						   v *= tmp.peek().getValue();
						   tmp.peek().setValue(v) ;    // check
						   
						}
					
					range--;
				}
				
				while(!tmp.empty())	stack.push(tmp.pop());
			
				selection.reset();
			}
			else 
			{
				   Double v = -1.00 ;
				   v *= stack.peek().getValue();
				   stack.peek().setValue(v) ;    	
			}
			expressionMode.off();
			return "changed";    
			  
	 }
	 public String expression(int mode)
		{
			String result = "" ;

			if(size()==0) return " 0 | Start new computation" ;
			
				   
				   format.reset( stack.peek().getValue() ); 
				   String value = format.format();
				   
				   if(totalOprands>0)  // show the current entries yet to be computated
				   {
					   Stack<Double>  tmp = pick(totalOprands);
					   int size = tmp.size();
					   String[] trv = new String[size];
						 while(!tmp.empty())
						 {
						   format.reset(tmp.pop());
						   trv[tmp.size()] = format.format();  // putting in left - right order
						    
						 }
						result =  trv[0];
					    for(int i = 1; i < trv.length; i++) result += ","+trv[i];
					    
				   }
				   else   // show last computation
				   {
					  
					   expressionMode.on();  // expand mode
					  if(mode==COMPRESS)expressionMode.change(COMPRESS);  // change it to compress mode
					  System.out.println("expression() :");
					 
					  if(expressionMode.currentMode()==EXPAND) result = expressionType.express(stack.peek(),true );
					  else result = expressionType.express(stack.peek(),false );
					 
				   }
				   
			  return  (value + " | " + result) ;
		}
	 public String state()
	 {
		 String result;
		 if(expressionMode.active())result = expression(changeExpression());			 
		 else
		 {
			if(totalOprands>0) result = "current oprands = "+span();
			else result = "total computations = "+span();
		 }
		 return result;
	 }
	 private int changeExpression()
	    {   		
	      return ( (expressionMode.currentMode()+1)%2 );
	    }
	 
	 private int occurance(String word, String character )
	 {
	 		return  word.length() - word.replace(character, "").length();		
	 }
	 
	  	
	 	
	 public int size()
     {
		return stack.size();	
	 }
	 
	 public double pi()
     {
		 expressionMode.off();
	    return PI ;		
	 }
	 
	 private int span()  // maximum distance/range to search for desired items in stack  
     {
		 if(totalOprands>0) return totalOprands ;  
		 
		 return totalComputations;
		 
	 }
	 
	 
	 // reverse the order of a stack
	 private Stack<Action<Double>> invert(Stack<Action<Double>> stack) 
     {
		 Stack<Action<Double>> reverse = new  Stack<Action<Double>>();
		 while(!stack.empty())	reverse.push(stack.pop());
		 return reverse ;		
	 }
	 private void shuffle(Action<Double> act) 
     {
		 Stack<Action<Double>> reverse = new  Stack<Action<Double>>();
		 while(!deleted.empty())	reverse.push(deleted.pop());
		 deleted.push(act);
		 while(!reverse.empty())	deleted.push(reverse.pop());
		 		
	 }
	
	
}

	


