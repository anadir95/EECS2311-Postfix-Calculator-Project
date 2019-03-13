/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class is the Controller   
 *  =================================================== 
 */

import java.util.StringTokenizer;

public class Controller
{
	private View view;
	
	private DisplayFormat format;
	private int prefix;
	private boolean graph;
	
	protected Model model;
	
	
	
	public Controller(Model m, View v)
	{
		model =   m;
		view =  v ;
		view.setMonitor(this);
		format = new DisplayFormat(0.00);
		prefix = 0;
		graph = false;
	}
      
	public void inform(Message msg)
	{  
		
		
		String reply;
		
		if(msg.getSource().equalsIgnoreCase("clear"))  
		{ 
		     model.purge() ; 
			 view.clear();
			 view.setFieldState(true);
			 if(graph)
		     {
		    	 view.changeGraph(false);
		    	 graph = false;
		     }
		}  
		
		else if(msg.getSource().equalsIgnoreCase("select")) 
			{
			      if(model.size()>0)
				   { 
			    	    if( valid( msg.getContent() ) ) {model.select(msg.getContent());view.clear(); }
						else view.display("Syntex error !"); // if select button pressed after a msg rather than inout
				   }
			      else view.display("Nothing to select !");
			      if(graph)
				     {
				    	 view.changeGraph(false);
				    	 graph = false;
				     }
			}
	    
		else if(msg.getSource().equalsIgnoreCase("undo")) // undo
		     {
			   if(!valid(msg.getContent()))view.clear();
			   {
			    if((msg.getContent()).equalsIgnoreCase("empty")|| !valid(msg.getContent())    ) // delete some previous action
			    	{  
			    	  reply = model.remove();
			    	  if(!reply.equalsIgnoreCase("removed"))view.display(reply);			  
			    	}
		         else  // backspace 
		          {
		        	if(  (msg.getContent()).equalsIgnoreCase("expr")  ) 
		        	  {
		        		view.display(model.expression(0));
		        		view.setFieldState(false);
		        	  }
		        	else 
		        	{ 
		        		
		        		view.backspace(); 
		        		view.setFieldState(true);
		        	}
		        	
		          }	
			    if(graph)
			     {
			    	 view.changeGraph(false);
			    	 graph = false;
			     }
			   }
			 }
			  
		else if(msg.getSource().equalsIgnoreCase("+")) 
		    {
			    if(model.size()>0)
			    {
			      String cp = msg.getContent() ;			      
			      if(!msg.empty() && valid(cp)) model.insert(Double.parseDouble(cp));
			      model.add();
			      view.display(model.expression(0));
			    }
			    else view.display("Nothing to add !");
			    if(graph)
			     {
			    	 view.changeGraph(false);
			    	 graph = false;
			     }
		     }
		
		else if(msg.getSource().equalsIgnoreCase("-")) 
		 {
			if(model.size()>0)
		    {
			  
				String cp = msg.getContent() ;
				if(!msg.empty() && valid(cp)) model.insert(Double.parseDouble(cp));
			  
		      model.subtract();
			  view.display(model.expression(0));
		    }
			else view.display("Nothing to subtract !");
			 if(graph)
		     {
		    	 view.changeGraph(false);
		    	 graph = false;
		     }
		 }  
		
		else if(msg.getSource().equalsIgnoreCase("x")) // x
		     {
			   if(model.size()>0)
		       {
				   String cp = msg.getContent() ;
				   if(!msg.empty() && valid(cp)) model.insert(Double.parseDouble(cp));
			     
			     model.multiply();
			     view.display(model.expression(0));
		       }
			   else view.display("Nothing to multiply !");
			   if(graph)
			     {
			    	 view.changeGraph(false);
			    	 graph = false;
			     }
		     }  
		else if(msg.getSource().equalsIgnoreCase("/"))  //  div
		     {
			  if(model.size()>0)
		      {
				  String cp = msg.getContent() ;		   
				  if(!msg.empty() && valid(cp)) model.insert(Double.parseDouble(cp));
				  model.divide();
		         view.display(model.expression(0)); 
		      } 
			  else  view.display("Nothing to divide !");
			  if(graph)
			     {
			    	 view.changeGraph(false);
			    	 graph = false;
			     }
		     }
		else if(msg.getSource().equalsIgnoreCase("sin"))  
	     {
		     prefix = 1;
	     }
		else if(msg.getSource().equalsIgnoreCase("cos"))  //  cos
	     {
		     prefix = 2;
	     }
		else if(msg.getSource().equalsIgnoreCase("enter"))// enter
		{
		    
			if ( msg.getContent().equalsIgnoreCase("empty") )view.display("No value entered !");
			else if(prefix>0) // sin or cos button pressed
			{
				 Double input = Double.parseDouble(msg.getContent()) ;
				 graph = true;
				 view.changeGraph(true);
				 format.reset(input);
				 if(format.hasFraction()) view.display("Integer value required !");
				 else 
				 {
					   int degree = Integer.parseInt(format.getIntegerPart());
					   GraphicalView  v = (GraphicalView) view ;
				        double r ;
				        if (prefix==1)
				        { 
				        	r  = model.sin(degree);
					        v.sin(degree,r);
				        }
				        else 
				        {
					          r  = model.cos(degree);
					          v.cos(degree,r);
				        }
																				
				 }
				 
				prefix=0;
			}			
			else  
				{
				   model.insert( Double.parseDouble(msg.getContent()) );
				   view.display( model.expression(0) );
				   if(graph)
				     {
				    	 view.changeGraph(false);
				    	 graph = false;
				     }  
				}
			  
		}
		else if(msg.getSource().equalsIgnoreCase("rad"))  
	     {
			 Double degree = Double.parseDouble(msg.getContent()) ;
			 format.reset(degree);
			 if(format.hasFraction()) view.display("Integer value required !");
			 else 
				 {
				    int rad = Integer.parseInt(msg.getContent());
				    format.reset( model.radian(rad) );
				    view.display(  format.format()  );
				 }
		     
	     }
		else if(msg.getSource().equalsIgnoreCase("Pi")) 
	     {
			format.reset(model.pi());	  
			view.display(  format.format()  );
				 		     
	     }
		else if(msg.getSource().equalsIgnoreCase("!"))  //  fact
	     {
			  Double degree = Double.parseDouble(msg.getContent()) ;
			  format.reset(degree);
			  if(format.hasFraction()) view.display("Integer value required !");
			  else 
				 {
				    int input = Integer.parseInt(msg.getContent());  
				    int output = model.factorial(input);
				    
				    view.display(  Integer.toString(output)   );
				    
				 }
		     if(graph)
		     {
		    	 view.changeGraph(false);
		    	 graph = false;
		     }
	     }
			
		else if(msg.getSource().equalsIgnoreCase("sign"))  //  +/-
	     {
			
			if ( msg.getContent().equalsIgnoreCase("empty") )
				{ 
				   reply = model.negate();
				   if ( !reply.equalsIgnoreCase("changed") ) view.display(reply);
				}
			else  // negate what user is typing,  not entered yet
			   {
				     Double negative = Double.parseDouble(msg.getContent()) * (-1);
				     
				     format.reset(negative);
				     view.display(format.format());
				     view.setFieldState(true);
			   }
			 if(graph)
		     {
		    	 view.changeGraph(false);
		    	 graph = false;
		     }
	     }
		else if(msg.getSource().equalsIgnoreCase("mode")) view.display(model.state());
		
	}
	
	

	private boolean valid(String str)
	{
		boolean result = true;
		
		for(int i = 0; i < str.length(); i++)  
		   {
					    if( !( Character.isDigit(str.charAt(i)) || str.charAt(i)=='.' ) )
					    {
			    		  result = false; 
			    		  break;
					    }
			    
			}

		return result;
	}
	  
}
