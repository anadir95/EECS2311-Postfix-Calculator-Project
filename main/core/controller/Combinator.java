/**     
 *  ================================================================================ 
 *  author :  Yari Yousefian
 *  This class represents the next segment of a sub-expression  in an expression 
 *  ================================================================================ 
 */

public class Combinator<T> 
{ 
		  private Action<T> action;
		  T value;
		  int operator;
		  
		  public Combinator(Action<T> act, T val, int op) 
		  { 
		    action = act; 
		    value = val; 
		    operator = op;
		  }
		  public Action<T> getAction()
		  {
			  return action;
		  }
		  public T getValue()
		  {
			  return value;
		  }
		  public void setOperator(int op)
		  {
			  operator = op;
		  }
		  public int operator()
		  {
			  return operator ;
		  }
} 