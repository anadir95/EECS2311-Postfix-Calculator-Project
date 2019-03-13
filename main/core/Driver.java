/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class runs the System   
 *  =================================================== 
 */


public class Driver
{
    public static void main(String[] args) 
    {
          View view = new GraphicalView();
          Model model = new Model(new Infix<Double>());
    	
    	  new Controller(model,view);
       
    }
}
