/**     
 *  ================================================================== 
 *  author :  Yari Yousefian
 *  This class is to pass messages between components of the system  
 *  =================================================================== 
 */
public class Message 
{   
    
    private String reply,state, content,source;
	
    
  
    public void reply(String rep)
    {
  	  reply = rep;
    }
    
    
    
    public void setState(String st)
    {
  	  state = st;
    }
    
    public String getState()
    {
  	  return state;
    }
    
    public void setContent(String cn)
    {
  	  content = cn;
    }
    
    public String getContent()
    {
  	  return content;
    }
    public boolean empty()
    {
  	  return content.equalsIgnoreCase("empty");
    }
    
    public void setSource(String src)
    {
  	  source = src;
    }
    
    public String getSource()
    {
  	  return source;
    }
    
	
}