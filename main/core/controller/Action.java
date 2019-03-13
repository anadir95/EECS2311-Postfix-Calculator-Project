import java.util.ArrayList;
import java.util.Stack;



/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class represents an action on claculator, each sub-expression   
 *  =================================================== 
 */

 // import java.util.ArrayList ;

 public class Action<T>
 {
	
	private final int OPRAND = 1;
	private final int COMPUTED = 2;
	private final int COMPOUND = 3;
	
	private T value;  
	private String localValue;   //  internal value before computing with another sub-exp[ression
	private String[] oprands ;  // all oprands involved if this is a result/computation 
	private int status;
	private Stack<Action<T>> previous;  // items deleted just before adding this action
	
	
	
	private int operator,rightOperator,leftOperator; // +=1, -=2, x=3, /=4
	private int index ;  // oprand position
	
	private Action<T> right;  // the next sub-expression computed with this 
	private ArrayList<  Combinator<T>  > terms;
	
	public Action(T val, String localval,  int op, int pos)
	{
		value = val;
		localValue =  localval;
		operator = op ;
		rightOperator = 0 ;
		leftOperator = 0 ;
		status = OPRAND;
		index = pos ;		
		oprands = new String[1];
		oprands[0]=localval;
		terms = new ArrayList<Combinator<T>>(7);
		previous = new Stack<Action<T>>();
	} 
	
	public int operator()
	{
	  return operator;
	}
	public int termsSize()
	{
	  return terms.size();
	}
	public int type()
	{
	  return status;
	}
	
	public void change(int stat)
	{
	   status = stat;
	}
	
	
	
	public int getRightOperator()
	{
	  return rightOperator;
	}
	public int getLeftOperator()
	{
	  return leftOperator;
	}
	public void setLeftOperator(int op)
	{
	  leftOperator = op;
	}
	public void setRightOperator(int op)
	{
	  rightOperator = op;
	} 
	public T getValue()
	{
	  return value ;
	}
	public String getLocalValue()
	{
	  return localValue ;
	}
	public void setValue(T val)
	{
	   value = val;	   
	} 
	
	public String[] getOprands()
	{
	  return oprands ;
	}
	public void setOprands( String[] op)
	{
	  oprands = op ;
	  status = COMPUTED;
	}
	
	public int position()
	{
	  return index ;
	}
	
	public boolean hasRight()
	{
	  return (rightOperator>0 && rightOperator<5) ;
	}
	public void setPosition(int pos)
	{
	  index = pos ;
	}
	public int lastOperator()
	{
	    int result;
		if(!terms.isEmpty()) result = (terms.get(terms.size())).operator() ;
		else if( hasRight() ) result = rightOperator;
		else result = operator;
		return result;
	}
	
	public Action<T> getRight()
	{
	  return right ;
	}
	public Combinator<T> term(int index)
	{
	  return terms.get(index) ;
	}
	private Action<T> split()
	{
		Action<T> detached = right;
		right = null ;
		if(oprands.length >1) status = COMPUTED;
	    else status = OPRAND;
		rightOperator = 0;
		return detached;
	}
	public void merge(Action<T> x,int op, T val)
	{
		if(!hasRight())
		{
		  right = x ;		
	      status = COMPOUND;
		  rightOperator = op;	
		  right.setLeftOperator(op);
		  value = val;
		  index = 0;
		}
		else  terms.add(new Combinator<T>(x,val,op)) ;
			
	}
	
	
	public Action<T> remove()
	{	  
		
		Action<T> removed = this;
		if(!terms.isEmpty()) removed = (terms.remove(terms.size()-1)).getAction();
		else if(hasRight()) removed = split();
		
	    return removed;
	}
	
	public Stack<Action<T>> getPrevious()
	{
	  return previous;
	}
	public void setPrevious(Stack<Action<T>> prev)
	{
	  previous = prev;
	}
	
 }

