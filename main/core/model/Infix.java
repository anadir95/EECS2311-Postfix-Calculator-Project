/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class represents Infix type of expression   
 *  =================================================== 
 */
public class Infix<T> implements Expression<T>
{
	
	public String express(Action<T> action, boolean expand) 
	{
		 System.out.println("current type : "+action.type());
	  
		int local = action.operator();
		
		int right = action.getRightOperator() ;
		int left = action.getLeftOperator() ;
		
		String localExpression,result;
		
		if(expand) localExpression =  convert( action.getOprands(),local ) ;
		else 
			{
			   localExpression = action.getLocalValue();
			   local = 5;  // not to bracket it
			}
		if(!action.hasRight()) return  localExpression;
		
		
		if(  (weight(local)<weight(right))   ) localExpression = "("+ localExpression + ")" ; 
		
		System.out.println("localExpr = "+localExpression);
		
		String rightExpression = express(action.getRight(),expand);
		
		if( weight(action.getRight().lastOperator())<weight(right) ) rightExpression = "("+ rightExpression + ")" ;
		System.out.println("r8Expr = "+rightExpression);
		
		result = localExpression +LABEL[right]+ rightExpression;
		System.out.println("rsult = "+result);
		int op = right;
		
		for(int i = 0; i <  action.termsSize(); i++) 
		{
			Combinator<T> trm = action.term(i);
			String expression = express(trm.getAction(),expand);
			int rt = trm.operator();
			if(  (weight(op)<weight(rt))) result = "("+ result + ")" ;
			if( weight(trm.getAction().lastOperator())<weight(rt) ) expression = "("+ expression + ")" ;
			result += LABEL[rt]+expression;
			op = rt;
		}
		
		
		return result;
	} 
	
	
	private String convert(String[] trv, int local)  // for localExpresiion
	{
		String result =  trv[0];
		System.out.println("printing convrt "+trv[0]);
		for(int i = 1; i < trv.length; i++) 
			{
			  result += LABEL[local]+trv[i];
			  System.out.println(trv[i]+",");
			}
		
		return result;
	}
	
	private int weight(int operator)
	{
		return (  ((operator % 2) + operator)  - 1 );
	}
	
}