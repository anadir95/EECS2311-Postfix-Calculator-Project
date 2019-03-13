/**     
 *  ================================================================================== 
 *  author :  Yari Yousefian
 *  This class represents type of calculator expressions such as postfix , infix, prefix, 
 *  expanded, compress etc   
 *  =================================================================================== 
 */

public interface Expression<T> 
{
	String[] LABEL = { "","+","-","x","/" };
	
	public String express(Action<T> actions, boolean expand) ;
}