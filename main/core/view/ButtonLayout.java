/**     
 *  ================================================================================================ 
 *  author :  Yari Yousefian
 *  This class creates the layout of button portion in frame, also creates and places the buttons   
 *  ================================================================================================== 
 */


import javax.swing.JPanel ;
import javax.swing.JButton ; 

public interface ButtonLayout 
{
	public JPanel[] designButtonLayout() ;
	public JButton[] getButtons(int i) ;
	public void createButtons(String[][] ragged );
}